package com.mzlalal.base.entity.oauth2.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户端VO
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:47
 */
@Data
@ApiModel("客户端VO")
public class ClientVo implements Serializable {
    private static final long serialVersionUID = -2879143853513564520L;

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;
    /**
     * 应用ID
     */
    @ApiModelProperty("应用ID")
    private String clientId;
    /**
     * 应用私匙
     */
    @ApiModelProperty("应用私匙")
    private String clientSecret;
    /**
     * 重定向地址
     */
    @ApiModelProperty("重定向地址")
    private String redirectUri;
    /**
     * 授权方式
     */
    @ApiModelProperty("授权方式")
    private String grantType;
    /**
     * 授权范围
     */
    @ApiModelProperty("授权范围")
    private String scope;
    /**
     * 允许保存的client访问,为空则允许全部
     */
    @ApiModelProperty("允许保存的client访问,为空则允许全部")
    private String resourceId;
    /**
     * 允许保存的role访问,为空则允许全部
     */
    @ApiModelProperty("允许保存的role访问,为空则允许全部")
    private String roleId;
    /**
     * token有效时间
     */
    @ApiModelProperty("token有效时间")
    private Integer accessTokenTime;
    /**
     * refresh_token有效时间
     */
    @ApiModelProperty("refresh_token有效时间")
    private Long refreshTokenTime;
    /**
     * 是否自动授权
     */
    @ApiModelProperty("是否自动授权")
    private Integer autoApprove;

}
