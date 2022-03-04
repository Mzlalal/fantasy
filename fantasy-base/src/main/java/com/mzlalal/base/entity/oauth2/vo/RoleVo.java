package com.mzlalal.base.entity.oauth2.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 角色VO
 *
 * @author Mzlalal
 * @date 2022-02-28 10:36:02
 */
@Data
@Builder
@ApiModel("角色VO")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo implements Serializable {
    private static final long serialVersionUID = -7508424069967910559L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("租户ID")
    private String tenantId;
}
