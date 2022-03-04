package com.mzlalal.base.entity.oauth2.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户端DTO
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:47
 */
@Data
@ApiModel("客户端DTO")
@TableName("t_client")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity implements Serializable {
    private static final long serialVersionUID = -3474524373938676799L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("应用标识")
    private String clientKey;

    @ApiModelProperty("应用私匙")
    private String clientSecret;

    @ApiModelProperty("应用名")
    private String clientName;

    @ApiModelProperty("首页地址")
    private String indexUri;

    @ApiModelProperty("重定向地址")
    private String redirectUri;

    @ApiModelProperty(value = "授权方式", example = "mail,password")
    private String responseType;

    @ApiModelProperty(value = "客户端图片")
    private String clientHeader;

    @ApiModelProperty(value = "授权范围", example = "mobile,mail,header")
    private String scope;

    @ApiModelProperty(value = "token有效时间(秒)", example = "86400")
    private Integer accessTokenTime;

    @ApiModelProperty(value = "refresh_token有效时间(秒)", example = "1296000")
    private Long refreshTokenTime;

    @ApiModelProperty(value = "是否自动授权", example = "0")
    private Integer autoApprove;

    @ApiModelProperty("创建人ID")
    private Long createBy;

    @ApiModelProperty("创建人")
    private String createName;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新人ID")
    private Long updateBy;

    @ApiModelProperty("更新人")
    private String updateName;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
