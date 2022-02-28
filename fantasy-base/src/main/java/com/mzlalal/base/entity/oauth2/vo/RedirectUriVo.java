package com.mzlalal.base.entity.oauth2.vo;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 二次确认URL返回结果
 *
 * @author Mzlalal
 * @date 2021/7/28 16:38
 */
@Data
@Builder
@ApiModel("二次确认URL返回结果")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RedirectUriVo extends BaseEntity {
    private static final long serialVersionUID = 7314359505039342617L;

    @ApiModelProperty("重定向地址")
    private String redirectUri;

    @ApiModelProperty("首页地址")
    private String indexUri;
}
