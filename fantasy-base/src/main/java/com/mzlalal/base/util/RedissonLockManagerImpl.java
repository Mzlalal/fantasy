package com.mzlalal.base.util;

import cn.hutool.core.lang.Assert;
import com.mzlalal.base.service.LockManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 锁管理器实现
 *
 * @author xiaoxiaolin
 */
@Slf4j
@Component
@AllArgsConstructor
public class RedissonLockManagerImpl implements LockManager {
    private final RedissonClient redissonClient;

    @Override
    public LockManager.Callback tryLock(String lockKey) {
        return this.tryLock(lockKey, 0, TimeUnit.SECONDS);
    }

    @Override
    public LockManager.Callback tryLock(String lockKey, long waitTime, TimeUnit timeUnit) {
        Assert.isTrue(waitTime >= 0, "锁等待时间必须大于或等于0");
        Assert.notBlank(lockKey, "锁名称不能为空");
        return runnable -> {
            RLock lock = this.redissonClient.getLock(lockKey);
            try {
                if (lock.tryLock(waitTime, timeUnit)) {
                    try {
                        runnable.run();
                    } finally {
                        // 释放锁
                        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                            lock.unlock();
                        }
                    }
                }
            } catch (InterruptedException e) {
                log.error("加锁失败,锁名称:{}", lockKey, e);
            }
        };
    }
}