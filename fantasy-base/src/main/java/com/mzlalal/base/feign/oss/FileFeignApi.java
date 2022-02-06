package com.mzlalal.base.feign.oss;

import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oss.vo.UploadFileVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件feign API
 *
 * @author Mzlalal
 * @date 2021/9/24 10:04
 */
@FeignClient(name = "FileFeignApi", url = "${fantasy-oss.feign.url}/api/v1/oss/file")
public interface FileFeignApi {

    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @return string 文件FS地址
     * @throws Exception IO异常
     */
    @ApiOperation("上传文件")
    @PostMapping(value = "/upload.file", headers = "content-type=multipart/form-data")
    @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File", paramType = "form")
    Result<UploadFileVo> upload(@RequestPart("file") MultipartFile multipartFile) throws Exception;
}
