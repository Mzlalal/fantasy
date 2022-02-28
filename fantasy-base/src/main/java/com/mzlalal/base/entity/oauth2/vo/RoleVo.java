package com.mzlalal.base.entity.oauth2.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 角色VO
 *
 * @author Mzlalal
 * @date 2022-02-28 10:36:02
 */
@Data
@Builder
@ApiModel("角色VO")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleVo extends BaseEntity {
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
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    private String tenantId;
}
