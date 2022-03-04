package com.mzlalal.base.entity.oauth2.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 生成用户令牌参数
 *
 * @author Mzlalal
 * @date 2021/7/28 14:34
 */
@Data
@Builder
@ApiModel("根据授权码生成用户令牌参数")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CreateTokenReq implements Serializable {
    private static final long serialVersionUID = -5009016199714464610L;

    @ApiModelProperty("客户端ID")
    @NotBlank(message = "客户端ID不能为空")
    private String clientKey;

    @ApiModelProperty("私匙")
    @NotBlank(message = "客户端私匙不能为空")
    private String clientSecret;

    @ApiModelProperty("授权方式,例:password-密码(直接返回TOKEN) mail-邮件验证码(二次验证)")
    @NotBlank(message = "授权方式不能为空")
    private String responseType;

    @ApiModelProperty("密码,例:文本密码/邮件验证码")
    @NotBlank(message = "密码不能为空")
    private String authorizeCode;
}
