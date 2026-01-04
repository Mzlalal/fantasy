package com.mzlalal.oss.service;

import com.mzlalal.base.entity.oss.dto.TodoCosmeticEntity;
import com.mzlalal.base.service.BaseService;

/**
 * 化妆品待办service
 *
 * @author Mzlalal
 * @date 2026-01-03 13:16:16
 */
public interface TodoCosmeticService extends BaseService<TodoCosmeticEntity> {

    /**
     * 对懒人模式的邮件进行提醒
     */
    void notifyLazyModeTodoList();

    /**
     * 对待办列表进行邮件通知
     */
    void notifyCurrentTimeTodoList();
}