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
 * 授权码回调参数
 *
 * @author Mzlalal
 * @date 2022/2/9 21:17
 */
@Data
@Builder
@ApiModel("授权码回调参数")
@NoArgsConstructor
@AllArgsConstructor
public class OauthCallbackReq implements Serializable {
    private static final long serialVersionUID = -7486074614031343046L;

    @ApiModelProperty("授权码")
    @NotBlank(message = "授权码不能为空")
    private String code;

    @ApiModelProperty("授权方式")
    @NotBlank(message = "授权方式不能为空")
    private String responseType;

    @ApiModelProperty("自定义参数")
    private String state;
}
