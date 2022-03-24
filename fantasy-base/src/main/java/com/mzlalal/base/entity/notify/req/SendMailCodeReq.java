package com.mzlalal.base.entity.notify.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 发送邮件验证码给用户
 *
 * @author Mzlalal
 * @date 2021/9/27 20:45:25
 */
@Data
@Builder
@ApiModel("发送邮件验证码参数")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SendMailCodeReq implements Serializable {
    private static final long serialVersionUID = -2518161278977360761L;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String mail;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
