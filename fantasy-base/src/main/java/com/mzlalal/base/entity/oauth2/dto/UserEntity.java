package com.mzlalal.base.entity.oauth2.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mzlalal.base.entity.oauth2.vo.ClientVo;
import com.mzlalal.base.entity.oauth2.vo.RoleVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户DTO
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 */
@Data
@ApiModel("用户DTO")
@TableName(value = "t_user", resultMap = "userMap")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {
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
    private String roleId;

    @ApiModelProperty("角色")
    @TableField(exist = false)
    private List<RoleVo> roleList;

    @ApiModelProperty("客户端")
    @TableField(exist = false)
    private List<ClientVo> clientList;

    @ApiModelProperty("租户ID")
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}