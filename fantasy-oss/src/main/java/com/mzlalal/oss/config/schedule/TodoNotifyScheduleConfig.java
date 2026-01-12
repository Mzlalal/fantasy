package com.mzlalal.oss.config.schedule;

import com.mzlalal.oss.service.TodoCosmeticService;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 化妆品待办提醒调度
 *
 * @author Mzlalal
 * @date 2026/1/4 15:10
 **/
@Configuration
@EnableScheduling
@AllArgsConstructor
public class TodoNotifyScheduleConfig {

    /**
     * 待办提醒service
     */
    private final TodoCosmeticService todoCosmeticService;
    /**
     * redissonClient连接
     */
    private final RedissonClient redissonClient;

    /**
     * 每五分钟的五秒后执行,例如: 01:05:05, 01:10:05, 01:15:05
     */
    @Scheduled(cron = "5 0/5 * * * ?")
    public void todoCosmeticSchedule() {
        
    }
}
