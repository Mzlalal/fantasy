package com.mzlalal.oss.service.todo;

import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import com.mzlalal.base.util.AssertUtil;

/**
 * 验证码方式
 *
 * @author Mzlalal
 * @date 2022/1/24 21:28
 */
public enum NotifyTypeVerifyEnum {
    /**
     * 无提醒
     */
    NONE() {
        @Override
        public void checkNotifyValue(TodoNotifyEntity todoNotify) {

        }
    },
    /**
     * 每年
     */
    YEAR() {
        @Override
        public void checkNotifyValue(TodoNotifyEntity todoNotify) {
            // 月日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyMonth(), todoNotify.getNotifyDay()
                    , todoNotify.getNotifyHour(), todoNotify.getNotifyMinute()), "月日时分都不能为空");
        }
    },
    /**
     * 每月
     */
    MONTH() {
        @Override
        public void checkNotifyValue(TodoNotifyEntity todoNotify) {
            // 日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyDay(), todoNotify.getNotifyHour()
                    , todoNotify.getNotifyMinute()), "日时分都不能为空");
        }
    },
    /**
     * 每周
     */
    WEEK() {
        @Override
        public void checkNotifyValue(TodoNotifyEntity todoNotify) {

        }
    },
    /**
     * 每日
     */
    DAY() {
        @Override
        public void checkNotifyValue(TodoNotifyEntity todoNotify) {
            // 月日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyHour(), todoNotify.getNotifyMinute())
                    , "时分都不能为空");
        }
    };

    /**
     * 验证待办提醒的值
     *
     * @param todoNotify 待办提醒
     */
    public abstract void checkNotifyValue(TodoNotifyEntity todoNotify);

    /**
     * 根据重复提醒周期notifyType获取
     *
     * @param notifyType 重复提醒周期
     * @return NotifyTypeVerifyEnum
     */
    public static NotifyTypeVerifyEnum getEnum(String notifyType) {
        for (NotifyTypeVerifyEnum value : NotifyTypeVerifyEnum.values()) {
            // 枚举名字和type是否相等
            if (StrUtil.equalsIgnoreCase(notifyType, value.name())) {
                return value;
            }
        }
        throw GlobalResult.TODO_NOTIFY_TYPE_NOT_CORRECT.boom();
    }
}
