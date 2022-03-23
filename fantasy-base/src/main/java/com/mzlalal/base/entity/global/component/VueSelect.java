package com.mzlalal.base.entity.global.component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Vue Select集合格式
 *
 * @author Mzlalal
 * @date 2022/3/23 20:37
 **/
@Data
@ApiModel("Vue Select集合")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class VueSelect {

    @ApiModelProperty("编码")
    private Object code;

    @ApiModelProperty("标签")
    private Object label;
}
