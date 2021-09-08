package com.mzlalal.base.entity.global.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 分页基础参数
 *
 * @author Mzlalal
 * @date 2021/6/17 15:59
 **/
@Data
@ApiModel("分页基础参数")
public class Po<T> {

    @ApiModelProperty("分页条件")
    protected T entity;

    @ApiModelProperty("分页参数")
    @NotNull(message = "分页参数不能为空")
    protected PageInfo pageInfo;
}
