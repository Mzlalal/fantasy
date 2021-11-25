package com.mzlalal.base.entity.notify;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * 发送登录验证码邮件给用户
 *
 * @author Mzlalal88
 * @date 数据
 */
@Data
@Builder
@ApiModel("token")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountAuthorizeCodeEntity extends BaseEntity {

    @NotBlank(message = "邮箱账户不能为空")
    @ApiModelProperty(value = "邮箱账户", example = "xx@xx.xx")
    private String account;

    @NotBlank(message = "授权码不能为空")
    @ApiModelProperty(value = "授权码")
    private String authorizeCode;
}
