package com.mzlalal.base.service;

import java.util.concurrent.TimeUnit;

/**
 * 锁管理器
 *
 * @author xiaoxiaolin
 */
public interface LockManager {
    /**
     * 加锁
     *
     * @param lockKey 锁名称
     * @return Callback
     */
    Callback tryLock(String lockKey);

    /**
     * 加锁
     *
     * @param lockKey  锁名称
     * @param waitTime 等待时间
     * @param timeUnit 时间单位
     * @return Callback
     */
    Callback tryLock(String lockKey, long waitTime, TimeUnit timeUnit);

    /**
     * 回调
     */
    interface Callback {
        /**
         * 回调
         *
         * @param runnable 异步线程
         */
        void then(Runnable runnable);
    }
}