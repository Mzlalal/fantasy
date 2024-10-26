package com.mzlalal.oss.config.schedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.notify.service.MailNotifyService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 法定节假日调休打开闹钟提醒
 *
 * @author Mzlalal
 */
@Configuration
@EnableScheduling
@AllArgsConstructor
public class OpenClockScheduleConfig {

    /**
     * redissonClient连接
     */
    private final RedissonClient redissonClient;
    /**
     * 邮件提醒service
     */
    private final MailNotifyService mailNotifyService;

    /**
     * 每天凌晨00:30执行
     */
    @Scheduled(cron = "0 30 0 1/1 * ?")
    public void openClockSchedule() {
        // 获取锁
        RLock lock = redissonClient.getLock(GlobalConstant.dayMatterSchedule());
        try {
            // 尝试获取锁,最大等待时间300秒,超过300秒自动释放
            boolean tryLock = lock.tryLock(0, TimeUnit.SECONDS);
            if (!tryLock) {
                // 获取锁失败
                return;
            }
            DateTime today = DateUtil.date();
            String todayWork = HttpUtil.get("https://api.haoshenqi.top/holiday/today");
            Assert.notBlank(todayWork, "获取今天是否工作日失败");
            this.checkDateIsWorkday(today, todayWork);

            DateTime tomorrow = DateUtil.tomorrow();
            String tomorrowWork = HttpUtil.get("https://api.haoshenqi.top/holiday/tomorrow");
            Assert.notBlank(tomorrowWork, "获取明天是否工作日失败");
            this.checkDateIsWorkday(tomorrow, tomorrowWork);
        } catch (Exception e) {
            // 忽略
        } finally {
            // 解锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void checkDateIsWorkday(DateTime dateTime, String work) {
        if (DateUtil.isWeekend(dateTime) && StrUtil.equals(work, "工作")) {
            // 邮件标题
            String subject = StrUtil.format("设置闹钟提醒");
            // 邮件内容
            String content = StrUtil.format("明天是周末,但是需要工作,需要设置闹钟");
            // 用户ID集合
            List<String> userIdList = StrUtil.split("541616661@qq.com,185784351@qq.com", ",");
            // 发送到邮箱
            mailNotifyService.sendText(userIdList, subject, content);
        }
    }
}
