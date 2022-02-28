package com.mzlalal.base.entity.oauth2.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 角色与客户端的关联关系DTO
 *
 * @author Mzlalal
 * @date 2022-02-28 22:48:43
 */
@Data
@Builder
@ApiModel("角色与客户端的关联关系DTO")
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_role_client")
@EqualsAndHashCode(callSuper = true)
public class RoleClientEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    @ApiModelProperty("ID")
    private String id;
    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private String roleId;
    /**
     * 客户端ID
     */
    @ApiModelProperty("客户端ID")
    private String clientId;

}
