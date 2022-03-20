package com.mzlalal.oss.service;

import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import com.mzlalal.base.service.BaseService;

/**
 * 待办提醒service
 *
 * @author Mzlalal
 * @date 2022-03-04 21:58:11
 */
public interface TodoNotifyService extends BaseService<TodoNotifyEntity> {

    /**
     * 对懒人模式的邮件进行提醒
     */
    void notifyLazyModeTodoList();

    /**
     * 对待办列表进行邮件通知
     */
    void notifyCurrentTimeTodoList();
}