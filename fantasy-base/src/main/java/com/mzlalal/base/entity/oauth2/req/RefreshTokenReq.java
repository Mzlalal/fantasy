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
 * 生成用户令牌参数
 *
 * @author Mzlalal
 * @date 2021/7/28 14:34
 */
@Data
@Builder
@ApiModel("根据授权码生成用户令牌参数")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenReq implements Serializable {
    private static final long serialVersionUID = -1809317545328696639L;

    @ApiModelProperty("刷新令牌")
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;

    @ApiModelProperty("客户端Key")
    @NotBlank(message = "客户端Key不能为空")
    private String clientKey;
}
