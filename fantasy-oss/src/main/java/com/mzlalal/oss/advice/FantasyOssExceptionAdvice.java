package com.mzlalal.oss.advice;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.mzlalal.base.advice.FantasyExceptionAdvice;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * OSS异常处理
 *
 * @author Mzlalal
 * @date 2021/9/24 11:11
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class FantasyOssExceptionAdvice extends FantasyExceptionAdvice {

    /**
     * 未知异常
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(MinioException.class)
    public Result<String> handleMinioException(HttpServletRequest request, Exception exception) {
        this.logExAndParams(request, exception);
        return GlobalResult.SEVER_ERROR.result(ExceptionUtil.getMessage(exception));
    }
}
