package com.mzlalal.oss.config.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.po.PageInfo;
import com.mzlalal.base.entity.oss.dto.DayMatterEntity;
import com.mzlalal.notify.service.MailNotifyService;
import com.mzlalal.oss.service.DayMatterService;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
     * 每天凌晨00:30执行
     */
    @Scheduled(cron = "0 30 0 1/1 * ?")
    public void dayMatterSchedule() {
        // 获取锁
        RLock lock = redissonClient.getLock(GlobalConstant.todoNotifySchedule());
        try {
            // 尝试获取锁,最大等待时间30秒,超过30秒自动释放
            boolean tryLock = lock.tryLock(0, 300, TimeUnit.SECONDS);
            if (!tryLock) {
                // 获取锁失败
                return;
            }
            // 查询所有
            long count = dayMatterService.count();
            // 每页查询
            int pageSize = 200;
            // 创建分页
            PageInfo pageInfo = PageInfo.builder().pageSize(pageSize).build();
            // 遍历
            for (int i = 0; i < (count / pageSize) + 1; i++) {
                // 创建分页信息
                dayMatterService.createPageQuery(pageInfo);
                // 查询
                List<DayMatterEntity> dayMatterList = dayMatterService.list();
                // 遍历查询
                dayMatterList.parallelStream().forEach(item -> {
                    // 至今距离天数
                    long betweenDay = DateUtil.betweenDay(item.getMatterDate(), DateUtil.date(), true);
                    // content
                    String content = StrUtil.format("{}已经{}天啦!", item.getMatterMemo(), betweenDay);
                    // 用户ID集合
                    List<String> userIdList = StrUtil.split(item.getMatterMailSet(), ",");
                    // 发送到邮箱
                    mailNotifyService.sendText(userIdList, "纪念日-Fantasy", content);
                });
            }
        } catch (InterruptedException ignored) {
            // 忽略
        } finally {
            // 解锁
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
