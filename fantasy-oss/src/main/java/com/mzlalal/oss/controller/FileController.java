package com.mzlalal.oss.controller;

import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oss.UploadFileEntity;
import com.mzlalal.base.feign.oss.FileFeignApi;
import com.mzlalal.minio.service.MinioService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 *
 * @author Mzlalal
 * @date 2021/9/24 10:01
 */
@Slf4j
@Api(tags = "文件")
@Validated
@RestController
@RequestMapping("/api/v1/oss/file")
public class FileController implements FileFeignApi {
    /**
     * minio上传服务
     */
    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

    @Override
    public Result<UploadFileEntity> upload(@RequestPart("file") MultipartFile multipartFile) throws Exception {
        // 上传
        String path = minioService.upload(multipartFile.getOriginalFilename()
                , multipartFile.getInputStream());
        // 构建返回结果
        UploadFileEntity entity = UploadFileEntity.builder()
                .path(path)
                .size(multipartFile.getSize())
                .build();
        return Result.ok(entity);
    }
}
