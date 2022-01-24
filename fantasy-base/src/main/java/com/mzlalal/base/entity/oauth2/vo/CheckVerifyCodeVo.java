package com.mzlalal.base.entity.oauth2.vo;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 文本密码登录校验验证码参数
 *
 * @author Mzlalal88
 * @date 2022/1/24 23:59:18
 */
@Data
@ApiModel("文本密码登录校验验证码参数")
@EqualsAndHashCode(callSuper = true)
public class CheckVerifyCodeVo extends BaseEntity {

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("用户看base64验证码图片输入的值")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
