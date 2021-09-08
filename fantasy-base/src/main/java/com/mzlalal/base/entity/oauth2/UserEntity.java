package com.mzlalal.base.entity.oauth2;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 */
@Data
@ApiModel("DTO")
@TableName("t_user")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("头像")
    private String header;

    @ApiModelProperty("电子邮箱")
    private String mail;

    @ApiModelProperty("访问令牌")
    private String accessToken;

    @ApiModelProperty("刷新令牌")
    private String refreshToken;

    @ApiModelProperty("令牌过期时间")
    private Date expireAt;

    @ApiModelProperty("是否删除")
    private Integer isHide;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("租户ID")
    private String tenantId;

    @ApiModelProperty("创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新人ID")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
