package com.mzlalal.base.entity.oauth2.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户VO
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 */
@Data
@ApiModel("用户VO")
public class UserVo implements Serializable {
    private static final long serialVersionUID = 641413141597915087L;

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("头像")
    private String header;

    @ApiModelProperty("电子邮箱")
    private String mail;

    @ApiModelProperty("是否删除")
    private Integer isHide;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("租户ID")
    private String tenantId;

}
