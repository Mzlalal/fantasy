package com.mzlalal.base.entity.oauth2.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 角色与客户端的关联关系DTO
 *
 * @author Mzlalal
 * @date 2022-02-28 22:48:43
 */
@Data
@Builder
@ApiModel("角色与客户端的关联关系DTO")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_role_client")
public class RoleClientEntity implements Serializable {
    private static final long serialVersionUID = -7253249042525667750L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("角色ID")
    private String roleId;

    @ApiModelProperty("客户端ID")
    private String clientId;

}
