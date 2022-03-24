package com.mzlalal.base.entity.oss.dto;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mzlalal.base.entity.global.component.VueSelect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 纪念日DTO
 *
 * @author Mzlalal
 * @date 2022-03-23 19:39:24
 */
@Data
@Builder
@ApiModel("纪念日DTO")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_day_matter", autoResultMap = true)
public class DayMatterEntity implements Serializable {
    private static final long serialVersionUID = 4081398648210481074L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("备注")
    @NotBlank(message = "备注不能为空")
    private String matterMemo;

    @ApiModelProperty("事件日期")
    @NotNull(message = "事件日期不能为空")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private Date matterDate;

    @ApiModelProperty("提醒间隔")
    @NotNull(message = "提醒间隔不能为空")
    private Integer matterInterval;

    @ApiModelProperty("置顶状态")
    @NotBlank(message = "置顶状态不能为空")
    private String matterTopStatus;

    @ApiModelProperty("提醒列表")
    private String matterMailSet;

    @ApiModelProperty("提醒列表")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    @NotEmpty(message = "提醒列表不能为空")
    private List<VueSelect> matterSelectSet;

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

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
