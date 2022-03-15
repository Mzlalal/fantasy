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
     * 提醒当前时间的待办
     */
    void notifyTodoListCurrentTime();
}