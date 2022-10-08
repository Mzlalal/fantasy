package com.mzlalal.oss.enums;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import com.mzlalal.base.util.AssertUtil;

import java.util.Date;

/**
 * 提醒周期方式并获取下次提醒时间
 *
 * @author Mzlalal
 * @date 2022/1/24 21:28
 */
public enum NotifyTypeEnum {
    /**
     * 提醒一次
     */
    NONE("0") {
        @Override
        public void checkAndCreateNextTime(TodoNotifyEntity todoNotify) {
            // 月日都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyMonth(), todoNotify.getNotifyDay()), "月日都不能为空");

            // 对比当前时间,不能是过去的时间
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // 当前时间必须在dateTime之前
            AssertUtil.isTrue(DateUtil.date().before(dateTime), "待办时间不能小于当前时间");
            // 设置下次提醒时间
            todoNotify.setNotifyExecTime(dateTime);
        }

        @Override
        public void createNextTime(Date date, TodoNotifyEntity todoNotify) {
            throw GlobalResult.SEVER_ERROR.boom();
        }
    },
    /**
     * 每年
     */
    YEAR("1") {
        @Override
        public void checkAndCreateNextTime(TodoNotifyEntity todoNotify) {
            // 月日都不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyMonth(), todoNotify.getNotifyDay()), "月日都不能为空");
            // 获取用户传递的时间并格式化
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // 创建下次提醒时间
            this.createNextTime(dateTime, todoNotify);
        }

        @Override
        public void createNextTime(Date date, TodoNotifyEntity todoNotify) {
            // 设置下次提醒时间
            this.offsetTimeWhenBeforeCurrentTime(todoNotify, date, 1, DateField.YEAR);
        }
    },
    /**
     * 每月
     */
    MONTH("2") {
        @Override
        public void checkAndCreateNextTime(TodoNotifyEntity todoNotify) {
            // 日不能为空
            AssertUtil.isTrue(StrUtil.isAllNotBlank(todoNotify.getNotifyDay(), todoNotify.getNotifyHour()
                    , todoNotify.getNotifyMinute()), "日不能为空");

            // 获取用户传递的时间并格式化
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // 创建下次提醒时间
            this.createNextTime(dateTime, todoNotify);
        }

        @Override
        public void createNextTime(Date date, TodoNotifyEntity todoNotify) {
            // 设置下次提醒时间
            this.offsetTimeWhenBeforeCurrentTime(todoNotify, date, 1, DateField.MONTH);
        }
    },
    /**
     * 每周
     */
    WEEK("3") {
        @Override
        public void checkAndCreateNextTime(TodoNotifyEntity todoNotify) {
            // 星期几不能为空
            AssertUtil.notNull(todoNotify.getNotifyWeekday(), "星期几不能为空");

            // 获取用户传递的时间并格式化
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // 先校正weekday, 用户的星期几 - 格式化的时间(星期几)
            dateTime.offset(DateField.DAY_OF_YEAR, todoNotify.getNotifyWeekday() - DateUtil.dayOfWeek(dateTime));
            // 创建下次提醒时间
            this.createNextTime(dateTime, todoNotify);
        }

        @Override
        public void createNextTime(Date date, TodoNotifyEntity todoNotify) {
            // 设置下次提醒时间
            this.offsetTimeWhenBeforeCurrentTime(todoNotify, date, 1, DateField.WEEK_OF_YEAR);
        }
    },
    /**
     * 每日
     */
    DAY("4") {
        @Override
        public void checkAndCreateNextTime(TodoNotifyEntity todoNotify) {
            // 获取用户传递的时间并格式化
            DateTime dateTime = this.formatTodoNotifyTime(todoNotify);
            // 创建下次提醒时间
            this.createNextTime(dateTime, todoNotify);
        }

        @Override
        public void createNextTime(Date date, TodoNotifyEntity todoNotify) {
            // 设置下次提醒时间
            this.offsetTimeWhenBeforeCurrentTime(todoNotify, date, 1, DateField.DAY_OF_YEAR);
        }
    };

    /**
     * 字典值
     */
    private final String code;

    NotifyTypeEnum(String code) {
        this.code = code;
    }

    /**
     * 验证并且生成下次待办提醒的时间
     *
     * @param todoNotify 待办提醒
     */
    public abstract void checkAndCreateNextTime(TodoNotifyEntity todoNotify);

    /**
     * 生成下次待办提醒时间
     *
     * @param date       参数时间
     * @param todoNotify 待办提醒
     */
    public abstract void createNextTime(Date date, TodoNotifyEntity todoNotify);

    /**
     * 前端传递的条件转换为待办时间
     * yyyy-MM-dd hh:mm格式
     *
     * @param todoNotify 待办事项
     * @return DateTime
     */
    public DateTime formatTodoNotifyTime(TodoNotifyEntity todoNotify) {
        // 给年月日判断是否有值,没值则使用当前时间的部分
        DateTime nowDate = DateUtil.date();
        int year = nowDate.year();
        int month = todoNotify.getNotifyMonth() == null ? nowDate.month() : Integer.parseInt(todoNotify.getNotifyMonth());
        int day = todoNotify.getNotifyDay() == null ? nowDate.dayOfMonth() : Integer.parseInt(todoNotify.getNotifyDay());
        String hour = todoNotify.getNotifyHour();
        String minute = todoNotify.getNotifyMinute();
        // 阳历(公历)
        if (StrUtil.equals(GlobalConstant.STATUS_ONE, todoNotify.getNotifyCalendarType())) {
            // 格式化
            String format = StrUtil.format("{}-{}-{} {}:{}", year, month, day, hour, minute);
            return DateUtil.parse(format);
        } else {
            // 阴历(农历)
            ChineseDate chineseDate = new ChineseDate(year, month, day);
            // 返回成公历
            return new DateTime(chineseDate.getGregorianDate())
                    // 设置时分
                    .offset(DateField.HOUR, Integer.parseInt(hour))
                    .offset(DateField.MINUTE, Integer.parseInt(minute));
        }
    }

    /**
     * 若参数时间小于当前时间后偏移指定时间,并递归调用直到大于当前时间位置
     * 若参数时间大于当前时间则返回原时间
     *
     * @param todoNotifyEntity 待办提醒
     * @param date             参数时间
     * @param offset           偏移数
     * @param dateField        偏移单位
     */
    public void offsetTimeWhenBeforeCurrentTime(TodoNotifyEntity todoNotifyEntity, Date date, int offset, DateField dateField) {
        switch (todoNotifyEntity.getNotifyCalendarType()) {
            case "1":
                // 计算公历
                date = this.offsetDate(date, offset, dateField);
                // 保存
                todoNotifyEntity.setNotifyExecTime(date);
                break;
            case "2":
                // 计算阴历时间
                date = this.offsetChineseDate(date, todoNotifyEntity);
                // 保存
                todoNotifyEntity.setNotifyExecTime(date);
                break;
            default:
                throw GlobalResult.TODO_NOTIFY_CALENDAR_TYPE_NOT_CORRECT.boom();
        }
    }

    /**
     * 计算公历的偏移时间
     *
     * @param date      参数时间
     * @param offset    偏移数
     * @param dateField 偏移单位
     * @return 计算后的日期
     */
    public Date offsetDate(Date date, int offset, DateField dateField) {
        // 参数时间小于当前时间
        if (date.before(DateUtil.date())) {
            // 偏移时间
            date = DateUtil.offset(date, dateField, offset);
            // 递归,直到提醒时间大于当前时间某个周期位置
            return this.offsetDate(date, offset, dateField);
        }
        return date;
    }

    /**
     * 计算农历的便宜时间
     *
     * @param date             参数时间
     * @param todoNotifyEntity 待办事项
     * @return 计算后的日期
     */
    public Date offsetChineseDate(Date date, TodoNotifyEntity todoNotifyEntity) {
        DateTime dateTime = new DateTime(date);
        int year = dateTime.year();
        int month = dateTime.month();
        int day = dateTime.dayOfMonth();
        // 阴历(农历)
        ChineseDate chineseDate = new ChineseDate(year, month, day);
        // 农历转换的公历日期
        Date gregorianDate = chineseDate.getGregorianDate();
        // 根据重复提醒周期处理
        switch (NotifyTypeEnum.getEnum(todoNotifyEntity.getNotifyType())) {
            // 每年
            case YEAR:
                while (gregorianDate.before(DateUtil.date())) {
                    chineseDate = new ChineseDate(year + 1, month, day);
                    gregorianDate = chineseDate.getGregorianDate();
                }
                break;
            // 每月
            case MONTH:
                while (gregorianDate.before(DateUtil.date())) {
                    chineseDate = new ChineseDate(year, month + 1, day);
                    gregorianDate = chineseDate.getGregorianDate();
                }
                break;
            // 每周(转换为公历逻辑)
            case WEEK:
                if (gregorianDate.before(DateUtil.date())) {
                    return this.offsetDate(new DateTime(gregorianDate)
                                    .offset(DateField.HOUR, Integer.parseInt(todoNotifyEntity.getNotifyHour()))
                                    .offset(DateField.MINUTE, Integer.parseInt(todoNotifyEntity.getNotifyMinute()))
                            , 1, DateField.WEEK_OF_YEAR);
                }
                break;
            // 每日(转换为公历逻辑)
            case DAY:
                if (gregorianDate.before(DateUtil.date())) {
                    return this.offsetDate(new DateTime(gregorianDate)
                                    .offset(DateField.HOUR, Integer.parseInt(todoNotifyEntity.getNotifyHour()))
                                    .offset(DateField.MINUTE, Integer.parseInt(todoNotifyEntity.getNotifyMinute()))
                            , 1, DateField.DAY_OF_YEAR);
                }
                break;
            // 异常
            default:
                throw GlobalResult.TODO_NOTIFY_TYPE_NOT_CORRECT.boom();
        }
        // 返回成公历
        return new DateTime(gregorianDate)
                .offset(DateField.HOUR, Integer.parseInt(todoNotifyEntity.getNotifyHour()))
                .offset(DateField.MINUTE, Integer.parseInt(todoNotifyEntity.getNotifyMinute()));
    }

    /**
     * 根据重复提醒周期notifyType获取
     *
     * @param notifyType 重复提醒周期
     * @return NotifyTypeVerifyEnum
     */
    public static NotifyTypeEnum getEnum(String notifyType) {
        for (NotifyTypeEnum value : NotifyTypeEnum.values()) {
            // 枚举名字和type是否相等
            if (StrUtil.equalsIgnoreCase(notifyType, value.code)) {
                return value;
            }
        }
        throw GlobalResult.TODO_NOTIFY_TYPE_NOT_CORRECT.boom();
    }
}