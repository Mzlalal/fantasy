package com.mzlalal.oss.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oss.UploadFileEntity;
import com.mzlalal.base.feign.oss.FileFeignApi;
import com.mzlalal.minio.service.MinioService;
import io.minio.errors.MinioException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
    public Result<UploadFileEntity> upload(MultipartFile file) throws MinioException, IOException {
        // 保存为临时文件
        File uploadFile = new File("/temp/" + file.getOriginalFilename());
        FileUtil.writeFromStream(file.getInputStream(), uploadFile);
        // 上传
        String path = minioService.upload(uploadFile);
        // 删除文件
        boolean delete = uploadFile.delete();
        if (!delete) {
            log.info(StrUtil.format("临时文件:{}删除失败", uploadFile.getAbsolutePath()));
        }
        // 构建返回结果
        UploadFileEntity entity = UploadFileEntity.builder().path(path).size(file.getSize()).build();
        return Result.ok(entity);
    }
}
