package com.mzlalal.base.entity.oauth2.dto;

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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 用户DTO
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 */
@Data
@ApiModel("用户DTO")
@TableName("t_user")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {
    private static final long serialVersionUID = 6149495996020661158L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @Length(min = 1, max = 8, message = "用户名长度需要在1和8之间")
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty("手机号")
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

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
