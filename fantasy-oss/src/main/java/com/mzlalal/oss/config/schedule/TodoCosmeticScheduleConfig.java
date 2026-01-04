package com.mzlalal.oss.config.schedule;

import com.mzlalal.oss.service.TodoNotifyService;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 待办提醒调度
 *
 * @author Mzlalal
 * @date 2022/3/17 21:32
 **/
@Configuration
@EnableScheduling
@AllArgsConstructor
public class TodoCosmeticScheduleConfig {

    /**
     * 待办提醒service
     */
    private final TodoNotifyService todoNotifyService;
    /**
     * redissonClient连接
     */
    private final RedissonClient redissonClient;

    /**
     * 每五分钟的五秒后执行,例如: 01:05:05, 01:10:05, 01:15:05
     */
    @Scheduled(cron = "15 0/30 * * * ?")
    public void todoNotifySchedule() {

    }
}
