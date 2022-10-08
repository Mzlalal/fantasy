package com.mzlalal.base.entity.oss.dto;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 待办提醒DTO
 *
 * @author Mzlalal
 * @date 2022-03-14 21:45:07
 */
@Data
@Builder
@ApiModel("待办提醒DTO")
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_todo_notify")
public class TodoNotifyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @NotBlank(message = "日历类别不能为空")
    @ApiModelProperty("日历类别,例:1=阳历(公历),2=阴历(农历)")
    private String notifyCalendarType;

    @NotBlank(message = "重复提醒周期不能为空")
    @ApiModelProperty("重复提醒周期,例:0=无提醒,1=每年,2=每月,3=每周,4=每日")
    private String notifyType;

    @ApiModelProperty("月")
    private String notifyMonth;

    @ApiModelProperty("日")
    private String notifyDay;

    @ApiModelProperty("星期几")
    private Integer notifyWeekday;

    @ApiModelProperty("时")
    @NotBlank(message = "小时不能为空")
    private String notifyHour;

    @ApiModelProperty("分(5的倍数)")
    @NotBlank(message = "分钟不能为空")
    private String notifyMinute;

    @NotNull(message = "重复提醒次数不能为空")
    @ApiModelProperty("重复提醒次数")
    private Integer notifyLazyModeTimes;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MINUTE_PATTERN, timezone = "GMT+8")
    @ApiModelProperty("下次提醒执行时间")
    private Date notifyExecTime;

    @ApiModelProperty("备注")
    private String notifyMemo;

    @NotBlank(message = "是否置顶不能为空")
    @ApiModelProperty("是否置顶,例:0=不置顶,1=置顶")
    private String notifyTopStatus;

    @NotBlank(message = "提醒后自动删除不能为空")
    @ApiModelProperty("提醒后自动删除,例:0=不删除,1=删除")
    private String notifyAfterDelete;

    @ApiModelProperty("发送给这些邮箱")
    private String notifyMailSet;

    @ApiModelProperty("是否删除")
    private Integer isHide;

    @ApiModelProperty("创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新人ID")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;

}
