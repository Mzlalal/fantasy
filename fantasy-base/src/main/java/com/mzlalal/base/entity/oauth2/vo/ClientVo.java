package com.mzlalal.base.entity.oauth2.vo;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户端VO
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:47
 */
@Data
@ApiModel("客户端VO")
@EqualsAndHashCode(callSuper = true)
public class ClientVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("应用ID")
    private String clientKey;

    @ApiModelProperty("应用名")
    private String clientName;

    @ApiModelProperty("首页地址")
    private String indexUri;

    @ApiModelProperty(value = "客户端图片")
    private String clientHeader;
}
