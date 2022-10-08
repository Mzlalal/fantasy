package com.mzlalal.base.entity.oauth2.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * oauth请求参数
 *
 * @author Mzlalal
 * @date 2021/7/28 14:34
 */
@Data
@Builder
@ApiModel("登录参数")
@NoArgsConstructor
@AllArgsConstructor
public class OauthReq implements Serializable {
    private static final long serialVersionUID = -8043411261618997959L;

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("客户端Key")
    @NotBlank(message = "客户端Key不能为空")
    private String clientKey;

    @ApiModelProperty("私匙")
    private String clientSecret;

    @ApiModelProperty("授权方式,例:password-密码(直接返回TOKEN) mail-邮件验证码(二次验证)")
    @NotBlank(message = "授权方式不能为空")
    private String responseType;

    @ApiModelProperty("密码,例:文本密码/邮件验证码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("重定向地址")
    private String redirectUri;

    @ApiModelProperty("首页地址")
    private String indexUri;

    @ApiModelProperty("原封不动的返回")
    private String state;
}
