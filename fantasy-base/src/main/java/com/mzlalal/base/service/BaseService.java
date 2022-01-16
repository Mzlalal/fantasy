package com.mzlalal.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.util.Page;
import org.apache.ibatis.logging.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 基础Service
 *
 * @author Mzlalal
 * @date 2021/6/17 16:48
 **/
public interface BaseService<T> extends IService<T> {

    /**
     * 根据参数查询分页
     *
     * @param po 分页参数
     * @return 分页数据
     */
    Page<T> queryPage(Po<T> po);

    /**
     * 从异步线程中获取当前条件查询的总行数
     *
     * @param future 异步线程
     * @param log    日志
     * @return int 总行数
     */
    default Long getTotalResult(Future<Long> future, Log log) {
        // 获取结果
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("获取Future异常", e);
        }
        return 0L;
    }
}
