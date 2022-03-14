package com.mzlalal.oss.service.todo;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
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
    NONE("0") {
        @Override
        public void checkNotifyValue(TodoNotifyEntity todoNotify) {
            // 月日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyMonth(), todoNotify.getNotifyDay()
                    , todoNotify.getNotifyHour(), todoNotify.getNotifyMinute()), "月日时分都不能为空");

            // 对比当前时间,不能是过去的时间
            DateTime nowDate = DateUtil.date();
            DateTime dateTime = DateUtil.parse(StrUtil.format("{}-{}-{} {}:{}", nowDate.year()
                    , todoNotify.getNotifyMonth(), todoNotify.getNotifyDay()
                    , todoNotify.getNotifyHour(), todoNotify.getNotifyMinute()));
            AssertUtil.isTrue(DateUtil.compare(nowDate, dateTime) >= 0, "待办时间不能小于当前时间");
        }
    },
    /**
     * 每年
     */
    YEAR("1") {
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
    MONTH("2") {
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
    WEEK("3") {
        @Override
        public void checkNotifyValue(TodoNotifyEntity todoNotify) {
            // 日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyWeekday(), todoNotify.getNotifyHour()
                    , todoNotify.getNotifyMinute()), "星期时分都不能为空");
        }
    },
    /**
     * 每日
     */
    DAY("4") {
        @Override
        public void checkNotifyValue(TodoNotifyEntity todoNotify) {
            // 月日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyHour(), todoNotify.getNotifyMinute())
                    , "时分都不能为空");
        }
    };

    private final String code;

    NotifyTypeVerifyEnum(String code) {
        this.code = code;
    }

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
            if (StrUtil.equalsIgnoreCase(notifyType, value.code)) {
                return value;
            }
        }
        throw GlobalResult.TODO_NOTIFY_TYPE_NOT_CORRECT.boom();
    }
}
