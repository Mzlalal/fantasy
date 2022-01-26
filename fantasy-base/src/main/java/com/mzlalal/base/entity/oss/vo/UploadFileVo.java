package com.mzlalal.base.entity.oss.vo;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
@EqualsAndHashCode(callSuper = true)
public class UploadFileVo extends BaseEntity {
    private static final long serialVersionUID = 2020375640032804322L;

    @ApiModelProperty("文件路径")
    private String path;

    @ApiModelProperty("文件大小")
    private Long size;

}
