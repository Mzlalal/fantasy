package com.mzlalal.oss.service.todo;

import cn.hutool.core.date.DateField;
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
     * 提醒一次
     */
    NONE("0") {
        @Override
        public void checkAndGenerateNotifyNextTime(TodoNotifyEntity todoNotify) {
            // 月日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyMonth(), todoNotify.getNotifyDay()
                    , todoNotify.getNotifyHour(), todoNotify.getNotifyMinute()), "月日时分都不能为空");

            // 对比当前时间,不能是过去的时间
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // 当前时间必须在dateTime之前
            AssertUtil.isTrue(DateUtil.date().before(dateTime), "待办时间不能小于当前时间");
            // 设置下次提醒时间
            todoNotify.setNotifyNextTime(dateTime);
        }
    },
    /**
     * 每年
     */
    YEAR("1") {
        @Override
        public void checkAndGenerateNotifyNextTime(TodoNotifyEntity todoNotify) {
            // 月日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyMonth(), todoNotify.getNotifyDay()
                    , todoNotify.getNotifyHour(), todoNotify.getNotifyMinute()), "月日时分都不能为空");

            // 对比当前时间
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // dateTime在当前时间之前,代表是过去的时间,往后推一年
            if (dateTime.before(DateUtil.date())) {
                dateTime.offset(DateField.YEAR, 1);
            }

            // 设置下次提醒时间
            todoNotify.setNotifyNextTime(dateTime);
        }
    },
    /**
     * 每月
     */
    MONTH("2") {
        @Override
        public void checkAndGenerateNotifyNextTime(TodoNotifyEntity todoNotify) {
            // 日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyDay(), todoNotify.getNotifyHour()
                    , todoNotify.getNotifyMinute()), "日时分都不能为空");

            // 对比当前时间
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // dateTime在当前时间之前,代表是过去的时间,往后推一月
            if (dateTime.before(DateUtil.date())) {
                dateTime.offset(DateField.MONTH, 1);
            }
            // 设置下次提醒时间
            todoNotify.setNotifyNextTime(dateTime);
        }
    },
    /**
     * 每周
     */
    WEEK("3") {
        @Override
        public void checkAndGenerateNotifyNextTime(TodoNotifyEntity todoNotify) {
            // 日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyWeekday(), todoNotify.getNotifyHour()
                    , todoNotify.getNotifyMinute()), "星期时分都不能为空");

            // 对比当前时间
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // 先校正星期几
            dateTime.offset(DateField.DAY_OF_MONTH, Integer.parseInt(todoNotify.getNotifyWeekday()) - DateUtil.dayOfWeek(dateTime));
            // dateTime在当前时间之前,代表是过去的时间,往后推七日
            if (dateTime.before(DateUtil.date())) {
                dateTime.offset(DateField.DAY_OF_YEAR, 7);
            }
            // 设置下次提醒时间
            todoNotify.setNotifyNextTime(dateTime);
        }
    },
    /**
     * 每日
     */
    DAY("4") {
        @Override
        public void checkAndGenerateNotifyNextTime(TodoNotifyEntity todoNotify) {
            // 月日时分都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyHour(), todoNotify.getNotifyMinute())
                    , "时分都不能为空");

            // 对比当前时间
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // dateTime在当前时间之前,代表是过去的时间,往后推一日
            if (dateTime.before(DateUtil.date())) {
                dateTime.offset(DateField.DAY_OF_YEAR, 1);
            }
            // 设置下次提醒时间
            todoNotify.setNotifyNextTime(dateTime);
        }
    };

    /**
     * 字典值
     */
    private final String code;

    NotifyTypeVerifyEnum(String code) {
        this.code = code;
    }

    /**
     * 验证并且生成下次待办提醒的时间
     *
     * @param todoNotify 待办提醒
     */
    public abstract void checkAndGenerateNotifyNextTime(TodoNotifyEntity todoNotify);

    /**
     * 前端传递的条件转换为待办时间
     * yyyy-MM-dd hh:mm格式
     *
     * @param todoNotify 待办事项
     * @return DateTime
     */
    public DateTime formatTodoNotifyTime(TodoNotifyEntity todoNotify) {
        // 给年月日判断是否有值 没值则使用当前时间的部分
        DateTime nowDate = DateUtil.date();
        String year = String.valueOf(nowDate.year());
        String month = todoNotify.getNotifyMonth() == null ? String.valueOf(nowDate.month()) : todoNotify.getNotifyMonth();
        String day = todoNotify.getNotifyDay() == null ? String.valueOf(nowDate.dayOfMonth()) : todoNotify.getNotifyDay();
        // 格式化
        return DateUtil.parse(StrUtil.format("{}-{}-{} {}:{}", year, month, day, todoNotify.getNotifyHour()
                , todoNotify.getNotifyMinute()));
    }

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
