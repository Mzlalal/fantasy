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

import java.io.Serializable;
import java.util.Date;

/**
 * 发票DTO
 *
 * @author Mzlalal
 * @date 2022-10-15 22:37:54
 */
@Data
@Builder
@ApiModel("发票DTO")
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_invoice")
public class InvoiceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("发票名")
    private String invoiceName;

    @ApiModelProperty("发票地址")
    private String invoiceUrl;

    @ApiModelProperty("发票源地址")
    private String invoiceSourceUrl;

    @ApiModelProperty("发票转发状态")
    private String invoiceForwardStatus;

    @ApiModelProperty("发票日期")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private Date invoiceDate;

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
