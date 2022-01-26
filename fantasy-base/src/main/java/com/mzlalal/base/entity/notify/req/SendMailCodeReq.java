package com.mzlalal.base.entity.notify.req;

import com.mzlalal.base.entity.global.BaseEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * 发送邮件验证码给用户
 *
 * @author Mzlalal
 * @date 2021/9/27 20:45:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SendMailCodeReq extends BaseEntity {
    private static final long serialVersionUID = -2518161278977360761L;

    @NotBlank(message = "账户不能为空,例:邮箱")
    private String username;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
