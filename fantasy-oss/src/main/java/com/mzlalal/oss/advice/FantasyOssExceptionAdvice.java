package com.mzlalal.oss.advice;

import com.mzlalal.base.advice.FantasyExceptionAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}
