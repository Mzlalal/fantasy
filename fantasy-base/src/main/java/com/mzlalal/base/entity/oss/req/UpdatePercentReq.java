package com.mzlalal.base.entity.oss.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 人名脱敏参数实体
 *
 * @author Mzlalal
 */
@Data
@ApiModel("人名脱敏参数")
public class UpdatePercentReq implements Serializable {

    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "id", required = true)
    private String id;

    @NotNull(message = "百分比不能为空")
    @ApiModelProperty("百分比")
    private Integer cosmeticPercent;
}
