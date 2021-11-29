package com.mzlalal.base.entity.oauth2;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
public class ClientEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("应用ID")
    private String clientId;

    @ApiModelProperty("应用私匙")
    private String clientSecret;

    @ApiModelProperty("重定向地址")
    private String redirectUri;

    @ApiModelProperty(value = "授权类型", example = "mail,password")
    private String responseType;

    @ApiModelProperty(value = "授权范围", example = "mobile,mail,header")
    private String scope;

    @ApiModelProperty("允许保存的client访问,为空则允许全部")
    private String resourceId;

    @ApiModelProperty("允许保存的role访问,为空则允许全部")
    private String roleId;

    @ApiModelProperty(value = "token有效时间(秒)", example = "86400")
    private Integer accessTokenTime;

    @ApiModelProperty(value = "refresh_token有效时间(秒)", example = "1296000")
    private Long refreshTokenTime;

    @ApiModelProperty(value = "是否自动授权", example = "0")
    private Integer autoApprove;

    @ApiModelProperty("创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新人ID")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
