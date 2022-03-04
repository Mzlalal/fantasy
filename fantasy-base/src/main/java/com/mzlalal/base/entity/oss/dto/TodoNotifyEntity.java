package com.mzlalal.base.entity.oss.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 待办提醒DTO
 *
 * @author Mzlalal
 * @date 2022-03-04 21:58:11
 */
@Data
@Builder
@ApiModel("待办提醒DTO")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_todo_notify")
public class TodoNotifyEntity implements Serializable {
    private static final long serialVersionUID = -6165208728756335195L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("提醒类型单位,例:1=每年,2=每月,3=每周,4=每日")
    private String notifyType;

    @ApiModelProperty("月")
    private String notifyMonth;

    @ApiModelProperty("日")
    private String notifyDay;

    @ApiModelProperty("时")
    private String notifyHour;

    @ApiModelProperty("分")
    private String notifyMinute;

    @ApiModelProperty("租户ID")
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @ApiModelProperty("创建人ID")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人ID")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
