package com.mzlalal.base.entity.oss.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 飞虹字符统计VO
 *
 * @author Mzlalal
 * @date 2025-05-21 00:14:07
 */
@Data
@Builder
@ApiModel("飞虹字符统计VO")
@NoArgsConstructor
@AllArgsConstructor
public class DiaryStatVo {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("总字数")
    private Long totalWord;
}
