package com.mzlalal.base.entity.oauth2.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 二次确认URL返回结果
 *
 * @author Mzlalal
 * @date 2021/7/28 16:38
 */
@Data
@Builder
@ApiModel("二次确认URL返回结果")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RedirectUriVo implements Serializable {
    private static final long serialVersionUID = 7314359505039342617L;

    @ApiModelProperty("重定向地址")
    private String redirectUri;

    @ApiModelProperty("首页地址")
    private String indexUri;
}
