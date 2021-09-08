package com.mzlalal.base.entity.oauth2.vo;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * TOKEN参数
 *
 * @author Mzlalal88
 * @date 2021/7/30 17:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("授权参数")
@EqualsAndHashCode(callSuper = true)
public class TokenVo extends BaseEntity {

    private static final long serialVersionUID = 3160719267834821232L;

    @ApiModelProperty("客户端ID")
    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    @ApiModelProperty("客户端私匙")
    @NotBlank(message = "来源客户端未建立合作")
    private String clientSecret;

    @ApiModelProperty("验证字段类型")
    @NotBlank(message = "验证字段类型不能为空")
    private String type;

    @ApiModelProperty("验证字段")
    @NotBlank(message = "验证字段不能为空")
    private String field;

    @ApiModelProperty("授权类型")
    @NotBlank(message = "非法的授权类型")
    private String responseType;
}
