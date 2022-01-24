package com.mzlalal.base.entity.oss;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 文件上传实体
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
public class UploadFileEntity extends BaseEntity {

    @ApiModelProperty("文件路径")
    private String path;

    @ApiModelProperty("文件大小")
    private Long size;

}
