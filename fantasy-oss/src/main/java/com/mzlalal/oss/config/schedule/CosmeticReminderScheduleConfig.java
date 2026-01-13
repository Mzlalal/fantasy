package com.mzlalal.oss.config.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.TemplateEngine;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.oss.dto.TodoCosmeticEntity;
import com.mzlalal.notify.service.MailNotifyService;
import com.mzlalal.oss.enums.NotifyTypeEnum;
import com.mzlalal.oss.service.TodoCosmeticService;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 化妆品提醒调度
 *
 * @author Mzlalal
 * @date 2026-01-13
 **/
@Slf4j
@Configuration
@EnableScheduling
@AllArgsConstructor
public class CosmeticReminderScheduleConfig {

    /**
     * 化妆品service
     */
    private final TodoCosmeticService todoCosmeticService;
    /**
     * 邮件提醒service
     */
    private final MailNotifyService mailNotifyService;
    /**
     * redissonClient连接
     */
    private final RedissonClient redissonClient;
    /**
     * 模板引擎
     */
    private final TemplateEngine templateEngine;

    /**
     * 每5分钟执行一次化妆品库存提醒
     * cron表达式: 秒 分 时 日 月 星期
     */
    @Scheduled(cron = "0/15 * * * * ?")
    public void cosmeticReminderSchedule() {
        // 获取锁
        RLock lock = redissonClient.getLock(GlobalConstant.cosmeticSchedule());
        try {
            // 尝试获取锁,最大等待时间30秒,超过300秒自动释放
            boolean tryLock = lock.tryLock(0, 300, TimeUnit.SECONDS);
            if (!tryLock) {
                // 获取锁失败
                log.warn("化妆品提醒定时任务获取锁失败");
                return;
            }

            log.info("开始执行化妆品提醒定时任务");

            // 获取当前时间信息
            Calendar now = Calendar.getInstance();
            int currentHour = now.get(Calendar.HOUR_OF_DAY);
            int currentMinute = now.get(Calendar.MINUTE);
            // 1=周日, 2=周一, ..., 7=周六
            int currentWeekday = now.get(Calendar.DAY_OF_WEEK);

            // 转换为数据库中的星期格式 (1=周一, 2=周二, ..., 7=周日)
            int dbWeekday = currentWeekday == 1 ? 7 : currentWeekday - 1;

            // 格式化时分
            String hourStr = String.format("%02d", currentHour);
            String minuteStr = String.format("%02d", currentMinute);

            log.info("当前时间: {}:{}, 星期{}", hourStr, minuteStr, dbWeekday);

            // 查询所有需要提醒的化妆品
            List<TodoCosmeticEntity> allCosmetics = todoCosmeticService.list(Wrappers.<TodoCosmeticEntity>lambdaQuery()
                    .eq(TodoCosmeticEntity::getNotifyHour, hourStr)
            );

            if (CollUtil.isEmpty(allCosmetics)) {
                log.info("当前时段没有需要提醒的化妆品");
                return;
            }

            log.info("查询到 {} 个可能需要提醒的化妆品", allCosmetics.size());

            // 按提醒类型过滤
            List<TodoCosmeticEntity> remindList = allCosmetics.stream()
                    .filter(cosmetic -> this.shouldRemind(cosmetic, dbWeekday))
                    .collect(Collectors.toList());

            if (CollUtil.isEmpty(remindList)) {
                log.info("过滤后没有需要提醒的化妆品");
                return;
            }

            log.info("过滤后有 {} 个化妆品需要提醒", remindList.size());

            // 按用户分组发送邮件
            Map<String, List<TodoCosmeticEntity>> userMap = remindList.stream()
                    .filter(cosmetic -> StrUtil.isNotBlank(cosmetic.getNotifyMailSet()))
                    .filter(cosmetic -> Validator.isEmail(cosmetic.getNotifyMailSet()))
                    .collect(Collectors.groupingBy(TodoCosmeticEntity::getNotifyMailSet));

            log.info("需要发送邮件给 {} 个用户", userMap.size());

            // 遍历每个用户发送邮件
            userMap.forEach((email, cosmeticList) -> {
                try {
                    sendReminderEmail(email, cosmeticList);
                    log.info("成功发送化妆品提醒邮件给: {}, 包含 {} 个化妆品", email, cosmeticList.size());
                } catch (Exception e) {
                    log.error("发送化妆品提醒邮件失败: {}", email, e);
                }
            });

            log.info("化妆品提醒定时任务执行完成");

        } catch (Exception e) {
            log.error("化妆品提醒定时任务执行异常", e);
        } finally {
            // 解锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 判断是否应该提醒
     *
     * @param cosmetic  化妆品信息
     * @param dbWeekday 当前星期几(1-7, 1=周一)
     * @return true=需要提醒, false=不需要提醒
     */
    private boolean shouldRemind(TodoCosmeticEntity cosmetic, int dbWeekday) {
        String notifyType = cosmetic.getNotifyType();

        // notifyType: "3"=每周, "4"=每天
        if (NotifyTypeEnum.DAY.getCode().equals(notifyType)) {
            // 每天都提醒
            return true;
        } else if (NotifyTypeEnum.WEEK.getCode().equals(notifyType)) {
            // 每周提醒,检查星期几是否匹配
            Integer notifyWeekday = cosmetic.getNotifyWeekday();
            return notifyWeekday != null && notifyWeekday == dbWeekday;
        }

        return false;
    }

    /**
     * 发送提醒邮件
     *
     * @param email        邮箱地址
     * @param cosmeticList 化妆品列表
     */
    private void sendReminderEmail(String email, List<TodoCosmeticEntity> cosmeticList) {
        // 构建邮件数据
        Map<String, Object> mailData = new HashMap<>();

        // 统计数据
        int totalCount = cosmeticList.size();
        int urgentCount = (int) cosmeticList.stream().filter(c -> c.getCosmeticPercent() < 20).count();
        int warningCount = (int) cosmeticList.stream().filter(c -> c.getCosmeticPercent() >= 20 && c.getCosmeticPercent() <= 80).count();

        mailData.put("totalCount", totalCount);
        mailData.put("urgentCount", urgentCount);
        mailData.put("warningCount", warningCount);

        // 化妆品列表数据
        List<Map<String, Object>> cosmeticMapList = cosmeticList.stream()
                .map(this::buildCosmeticMailData)
                .collect(Collectors.toList());

        mailData.put("cosmeticList", cosmeticMapList);

        // 化妆品列表URL
        mailData.put("cosmeticListUrl", "http://192.168.1.2:18000/fantasy-oss/hua-zhuang-pin-lie-biao.html");

        // 发送时间
        mailData.put("sendTime", DateUtil.formatDateTime(new Date()));

        // 渲染邮件模板
        String content = templateEngine.getTemplate("/cosmetic/cosmetic-reminder-email.html").render(mailData);

        // 发送邮件
        mailNotifyService.send(email, "💄 化妆品库存提醒 - Fantasy", content, true);
    }

    /**
     * 构建单个化妆品的邮件数据
     *
     * @param cosmetic 化妆品信息
     * @return 邮件数据Map
     */
    private Map<String, Object> buildCosmeticMailData(TodoCosmeticEntity cosmetic) {
        Map<String, Object> data = new HashMap<>();

        // 基本信息
        data.put("cosmeticName", cosmetic.getCosmeticName());
        data.put("cosmeticStock", cosmetic.getCosmeticStock());
        data.put("cosmeticPercent", cosmetic.getCosmeticPercent());
        data.put("cosmeticMemo", cosmetic.getCosmeticMemo());

        // 提醒时间格式化
        String notifyTime = formatNotifyTime(cosmetic);
        data.put("notifyTime", notifyTime);

        // 根据百分比判断状态
        int percent = cosmetic.getCosmeticPercent();

        if (percent < 20) {
            // 紧急
            data.put("stockLevel", "low-stock");
            data.put("progressClass", "low");
            data.put("percentColor", "#ff6b6b");
            data.put("isUrgent", true);
        } else if (percent <= 80) {
            // 预警
            data.put("stockLevel", "medium-stock");
            data.put("progressClass", "medium");
            data.put("percentColor", "#ffa726");
            data.put("isWarning", true);
        } else {
            // 充足
            data.put("stockLevel", "high-stock");
            data.put("progressClass", "high");
            data.put("percentColor", "#66bb6a");
            data.put("isGood", true);
        }

        return data;
    }

    /**
     * 格式化提醒时间
     *
     * @param cosmetic 化妆品信息
     * @return 格式化后的提醒时间
     */
    private String formatNotifyTime(TodoCosmeticEntity cosmetic) {
        String notifyType = cosmetic.getNotifyType();
        String time = cosmetic.getNotifyHour() + ":" + cosmetic.getNotifyMinute();

        if ("3".equals(notifyType)) {
            // 每周
            String[] weekdays = {"", "一", "二", "三", "四", "五", "六", "日"};
            Integer weekday = cosmetic.getNotifyWeekday();
            if (weekday != null && weekday >= 1 && weekday <= 7) {
                return "每周" + weekdays[weekday] + " " + time;
            }
            return "每周 " + time;
        } else if ("4".equals(notifyType)) {
            // 每天
            return "每天 " + time;
        }

        return time;
    }
}
