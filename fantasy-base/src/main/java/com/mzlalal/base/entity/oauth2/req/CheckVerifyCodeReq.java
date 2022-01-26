package com.mzlalal.base.entity.oauth2.req;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 密码登录校验验证码参数
 *
 * @author Mzlalal
 * @date 2022/1/24 23:59:18
 */
@Data
@ApiModel("检查验证码参数")
@EqualsAndHashCode(callSuper = true)
public class CheckVerifyCodeReq extends BaseEntity {
    private static final long serialVersionUID = 7890819661057272354L;

    @ApiModelProperty("客户端ID")
    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("用户看base64验证码图片输入的值")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
