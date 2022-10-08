package com.mzlalal.base.entity.global.component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Vue Select集合格式
 *
 * @author Mzlalal
 * @date 2022/3/23 20:37
 **/
@Data
@Builder
@ApiModel("Vue Select集合")
@NoArgsConstructor
@AllArgsConstructor
public class VueSelect implements Serializable {
    private static final long serialVersionUID = 5336237624306685930L;

    @ApiModelProperty("编码")
    @NotNull(message = "编码不能为空")
    private Object code;

    @ApiModelProperty("标签")
    @NotNull(message = "标签不能为空")
    private String label;
}
