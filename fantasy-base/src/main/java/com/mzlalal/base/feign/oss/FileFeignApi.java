package com.mzlalal.base.feign.oss;

import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oss.UploadFileEntity;
import io.minio.errors.MinioException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件feign API
 *
 * @author Mzlalal
 * @date 2021/9/24 10:04
 */
@FeignClient(value = GlobalConstant.FANTASY_OAUTH2 + "/api/v1/oss/file", url = "http://127.0.0.1:9005/")
public interface FileFeignApi {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return string 文件FS地址
     * @throws MinioException minio异常
     * @throws IOException    IO异常
     */
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile", paramType = "form")
    Result<UploadFileEntity> upload(@RequestPart("file") MultipartFile file) throws MinioException, IOException;
}
