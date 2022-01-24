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
@ApiModel("验证码信息")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VerifyCodeEntity extends BaseEntity {

    @NotBlank(message = "账户不能为空,例:手机号码/邮箱")
    @ApiModelProperty(value = "账户")
    private String username;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String code;
}
