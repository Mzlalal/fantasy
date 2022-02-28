package com.mzlalal.base.entity.oauth2.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * 角色DTO
 *
 * @author Mzlalal
 * @date 2022-02-28 10:36:02
 */
@Data
@Builder
@ApiModel("角色DTO")
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_role")
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    @ApiModelProperty("ID")
    private String id;
    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String name;
    /**
     * 角色用途
     */
    @ApiModelProperty("角色用途")
    private String memo;
    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    private String tenantId;
    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private String createBy;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createName;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新人ID
     */
    @ApiModelProperty("更新人ID")
    private String updateBy;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateName;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
