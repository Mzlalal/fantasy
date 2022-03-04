package com.mzlalal.base.entity.oauth2.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户令牌返回结果
 *
 * @author Mzlalal
 * @date 2021/7/29 17:08
 */
@Data
@Builder
@ApiModel("用户令牌返回结果")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken implements Serializable {
    private static final long serialVersionUID = 7010075157513317532L;

    @ApiModelProperty("访问令牌")
    private String accessToken;

    @ApiModelProperty("刷新令牌")
    private String refreshToken;

    @ApiModelProperty("令牌过期时间")
    private Date expireAt;
}
