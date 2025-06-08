package com.mzlalal.oss.config.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.po.PageInfo;
import com.mzlalal.base.entity.oss.dto.DayMatterEntity;
import com.mzlalal.notify.service.MailNotifyService;
import com.mzlalal.oss.service.DayMatterService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 纪念日调度
 *
 * @author Mzlalal
 * @date 2022/3/25 21:12
 **/
@Configuration
@EnableScheduling
@AllArgsConstructor
public class DayMatterScheduleConfig {

    /**
     * 纪念日service
     */
    private final DayMatterService dayMatterService;
    /**
     * 邮件提醒service
     */
    private final MailNotifyService mailNotifyService;
    /**
     * redissonClient连接
     */
    private final RedissonClient redissonClient;

    /**
     * 每天凌晨00:15执行
     */
    @Scheduled(cron = "0 15 0 1/1 * ?")
    public void dayMatterSchedule() {
        // 获取锁
        RLock lock = redissonClient.getLock(GlobalConstant.dayMatterSchedule());
        try {
            // 尝试获取锁,最大等待时间30秒,超过30秒自动释放
            boolean tryLock = lock.tryLock(0, 300, TimeUnit.SECONDS);
            if (!tryLock) {
                // 获取锁失败
                return;
            }
            // 每页查询
            int pageSize = 200;
            // 创建分页信息
            Page<DayMatterEntity> page = dayMatterService.createPageQuery(PageInfo.builder().pageSize(pageSize).build());
            // 查询
            List<DayMatterEntity> dayMatterList = dayMatterService.list();
            // 遍历
            int currentPage = 1;
            // 小于等于当前页
            while (currentPage <= page.getPages()) {
                // 遍历查询
                dayMatterList.parallelStream().forEach(item -> {
                    // 至今距离天数
                    Long betweenDay = DateUtil.betweenDay(item.getMatterDate(), DateUtil.date(), true);
                    // 提醒间隔
                    Integer interval = item.getMatterInterval();
                    // 如果提醒间隔为0 或者 相距天数与提醒间隔余数不等于则不需要提醒
                    if (interval <= 0 || betweenDay.intValue() % interval != 0) {
                        return;
                    }
                    // 邮件标题
                    String subject = StrUtil.format("纪念日-{}-Fantasy", item.getMatterMemo());
                    // 邮件内容
                    String formattedDuration = formatDaysToDuration(betweenDay);
                    String content = StrUtil.format("{}已经{}天({})啦! ", item.getMatterMemo(), betweenDay, formattedDuration);
                    // 用户ID集合
                    List<String> userIdList = StrUtil.split(item.getMatterMailSet(), ",");
                    // 发送到邮箱
                    mailNotifyService.sendText(userIdList, subject, content);
                });
                // 创建分页信息
                dayMatterService.createPageQuery(PageInfo.builder().currPage(++currentPage).pageSize(pageSize).build());
                // 查询
                dayMatterList = dayMatterService.list();
            }
        } catch (InterruptedException ignored) {
            // 忽略
        } finally {
            // 解锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 格式化天数为年月日格式（考虑闰年和实际月份天数）
     *
     * @param totalDays 总天数
     * @return 格式化后的字符串，如"1年2月3天"
     */
    private String formatDaysToDuration(Long totalDays) {
        if (totalDays == null || totalDays <= 0) {
            return "0天";
        }

        // 使用当前日期作为结束日期，向前推算起始日期
        java.util.Date endDate = DateUtil.date();
        java.util.Date startDate = DateUtil.offsetDay(endDate, -totalDays.intValue());

        // 使用Calendar进行精确计算
        java.util.Calendar startCal = java.util.Calendar.getInstance();
        java.util.Calendar endCal = java.util.Calendar.getInstance();
        startCal.setTime(startDate);
        endCal.setTime(endDate);

        // 计算年份差
        int years = endCal.get(java.util.Calendar.YEAR) - startCal.get(java.util.Calendar.YEAR);

        // 计算月份差
        int months = endCal.get(java.util.Calendar.MONTH) - startCal.get(java.util.Calendar.MONTH);

        // 计算天数差
        int days = endCal.get(java.util.Calendar.DAY_OF_MONTH) - startCal.get(java.util.Calendar.DAY_OF_MONTH);

        // 如果天数为负，从月份借位
        if (days < 0) {
            months--;
            // 获取上个月的最大天数
            java.util.Calendar tempCal = (java.util.Calendar) endCal.clone();
            tempCal.add(java.util.Calendar.MONTH, -1);
            int maxDaysInPrevMonth = tempCal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
            days += maxDaysInPrevMonth;
        }

        // 如果月份为负，从年份借位
        if (months < 0) {
            years--;
            months += 12;
        }

        StringBuilder result = new StringBuilder();
        if (years > 0) {
            result.append(years).append("年");
        }
        if (months > 0) {
            result.append(months).append("月");
        }
        if (days > 0) {
            result.append(days).append("天");
        }

        // 如果结果为空（理论上不会发生，但为了安全起见）
        if (result.length() == 0) {
            return "0天";
        }

        return result.toString();
    }
}
