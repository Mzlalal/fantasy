package com.mzlalal.base.entity.oauth2.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
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
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_role")
public class RoleEntity implements Serializable {
    private static final long serialVersionUID = -443651908011920824L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色用途")
    private String memo;

    @ApiModelProperty("租户ID")
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @ApiModelProperty("创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新人ID")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
