package com.mzlalal.base.entity.oauth2;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * 授权TOKEN
 *
 * @author Mzlalal88
 * @date 2021/7/29 17:08
 */
@Data
@Builder
@ApiModel("用户Token")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccessToken extends BaseEntity {
    private static final long serialVersionUID = 7010075157513317532L;

    @ApiModelProperty("访问令牌")
    private String accessToken;

    @ApiModelProperty("刷新令牌")
    private String refreshToken;

    @ApiModelProperty("令牌过期时间")
    private Date expireAt;
}
