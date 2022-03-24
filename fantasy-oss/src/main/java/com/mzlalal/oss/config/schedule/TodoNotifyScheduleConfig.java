package com.mzlalal.oss.config.schedule;

import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.oss.service.TodoNotifyService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

/**
 * 待办提醒调度
 *
 * @author Mzlalal
 * @date 2022/3/17 21:32
 **/
@Configuration
@EnableScheduling
public class TodoNotifyScheduleConfig {
    /**
     * 待办提醒service
     */
    private final TodoNotifyService todoNotifyService;
    /**
     * redissonClient连接
     */
    private final RedissonClient redissonClient;

    public TodoNotifyScheduleConfig(TodoNotifyService todoNotifyService, RedissonClient redissonClient) {
        this.todoNotifyService = todoNotifyService;
        this.redissonClient = redissonClient;
    }

    /**
     * 每五分钟的五秒后执行,例如: 01:05:05, 01:10:05, 01:15:05
     */
    @Scheduled(cron = "5 0/5 * * * ?")
    public void todoNotifySchedule() {
        // 获取锁
        RLock lock = redissonClient.getLock(GlobalConstant.todoNotifyScheduleRedisKey());
        try {
            // 尝试获取锁,最大等待时间30秒,超过30秒自动释放
            boolean tryLock = lock.tryLock(30, 300, TimeUnit.SECONDS);
            if (!tryLock) {
                // 获取锁失败
                return;
            }
            // 对懒人模式的邮件进行提醒
            todoNotifyService.notifyLazyModeTodoList();
            // 对待办列表进行邮件通知
            todoNotifyService.notifyCurrentTimeTodoList();
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
