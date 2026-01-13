package com.mzlalal.base.entity.oss.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 化妆品待办DTO
 *
 * @author Mzlalal
 * @date 2026-01-03 13:33:07
 */
@Data
@Builder
@ApiModel("化妆品待办DTO")
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_todo_cosmetic")
public class TodoCosmeticEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @NotBlank(message = "化妆品名称不能为空")
    @ApiModelProperty("化妆品名称")
    private String cosmeticName;

    @NotNull(message = "库存不能为空")
    @ApiModelProperty("库存")
    private Integer cosmeticStock;

    @NotNull(message = "百分比不能为空")
    @ApiModelProperty("百分比")
    private Integer cosmeticPercent;

    @ApiModelProperty("备注")
    private String cosmeticMemo;

    @ApiModelProperty("是否置顶,例:0=不置顶,1=置顶")
    private Integer cosmeticTopStatus;

    @NotBlank(message = "重复提醒周期不能为空")
    @ApiModelProperty("重复提醒周期,例:0=无提醒,1=每年,2=每月,3=每周,4=每日")
    private String notifyType;

    @ApiModelProperty("星期几")
    private Integer notifyWeekday;

    @ApiModelProperty("时")
    @NotBlank(message = "小时不能为空")
    private String notifyHour;

    @ApiModelProperty("分(5的倍数)")
    @NotBlank(message = "分钟不能为空")
    private String notifyMinute;

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