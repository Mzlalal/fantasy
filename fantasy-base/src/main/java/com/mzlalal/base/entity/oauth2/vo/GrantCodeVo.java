package com.mzlalal.base.entity.oauth2.vo;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * code授权返回结果
 *
 * @author Mzlalal88
 * @date 2021/7/28 16:38
 */
@Data
@Builder
@ApiModel("code授权返回结果")
@EqualsAndHashCode(callSuper = true)
public class GrantCodeVo extends BaseEntity {
    private static final long serialVersionUID = 7314359505039342617L;

    @ApiModelProperty("重定向地址")
    private String redirectUri;
}
