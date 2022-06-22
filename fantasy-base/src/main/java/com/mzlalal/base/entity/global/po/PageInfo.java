package com.mzlalal.base.entity.global.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页参数
 *
 * @author Mzlalal
 * @date 2021/6/17 16:09
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页参数")
public class PageInfo {

    /**
     * 每页展示的行数
     */
    @Min(value = 1, message = "每页展示的行数pageSize最小不能小于1")
    @Max(value = 50, message = "每页展示的行数pageSize最大不能大于50")
    @ApiModelProperty(value = "每页展示的行数", example = "10")
    @Builder.Default
    protected int pageSize = 10;

    /**
     * 当前页
     */
    @Min(value = 1, message = "当前页码最小不能小于1")
    @ApiModelProperty(value = "当前页码", example = "1")
    @Builder.Default
    protected int currPage = 1;
}
