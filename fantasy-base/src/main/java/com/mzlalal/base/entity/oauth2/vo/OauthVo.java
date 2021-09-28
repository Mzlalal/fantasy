package com.mzlalal.base.entity.oauth2.vo;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * oauth请求参数
 *
 * @author Mzlalal88
 * @date 2021/7/28 14:34
 */
@Data
@ApiModel("授权参数")
@EqualsAndHashCode(callSuper = true)
public class OauthVo extends BaseEntity {

    private static final long serialVersionUID = -8043411261618997959L;

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("客户端ID")
    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    @ApiModelProperty("私匙")
    private String clientSecret;

    @ApiModelProperty("授权类型:password-密码(直接返回TOKEN) mail-邮件验证码(二次验证)")
    @NotBlank(message = "授权类型不能为空")
    private String grantType;

    @ApiModelProperty("验证码:password-密码 mail-邮件验证码")
    @NotBlank(message = "授权验证码不能为空")
    private String grantValue;

    @ApiModelProperty("重定向地址")
    private String redirectUri;

    @ApiModelProperty("原封不动的返回")
    private String state;
}
