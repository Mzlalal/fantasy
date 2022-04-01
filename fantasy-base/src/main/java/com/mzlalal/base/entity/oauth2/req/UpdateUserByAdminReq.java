package com.mzlalal.base.entity.oauth2.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 管理员重置用户部分信息
 *
 * @author Mzlalal
 * @date 2022/4/1 11:50
 */
@Data
@ApiModel("管理员重置用户部分信息请求")
public class UpdateUserByAdminReq implements Serializable {

    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "角色不能为空")
    @ApiModelProperty("角色ID")
    private String roleId;
}
