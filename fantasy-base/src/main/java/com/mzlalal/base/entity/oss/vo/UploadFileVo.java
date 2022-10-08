package com.mzlalal.base.entity.oss.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 上传文件返回结果
 *
 * @author Mzlalal
 * @date 2021/9/24 16:44
 */
@Data
@Builder
@ApiModel("上传文件返回结果")
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileVo implements Serializable {
    private static final long serialVersionUID = 2020375640032804322L;

    @ApiModelProperty("文件路径")
    private String path;

    @ApiModelProperty("文件大小")
    private Long size;

}
