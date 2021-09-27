package com.mzlalal.base.advice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.exception.BoomException;
import feign.FeignException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 异常统一处理
 *
 * @author Mzlalal
 * @date 2021/5/23 11:43
 **/
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class FantasyExceptionAdvice {

    /**
     * 打印系统异常和当前请求路径,请求参数
     *
     * @param request 请求
     * @param cause   异常
     */
    protected void logExAndParams(HttpServletRequest request, Exception cause) {
        // 打印原始报错
        log.error("", ExceptionUtil.getRootCause(cause));
        this.logCustomFormatMessage(request, cause);
    }

    /**
     * 打印当前请求路径,请求参数
     *
     * @param request 请求
     * @param cause   异常
     */
    protected void logCustomFormatMessage(HttpServletRequest request, Exception cause) {
        // 输出格式
        String format = "\n\t请求路径:{}\n\t代码位置:{}\n\t请求参数:\n\t\turl:{}\n\t\tbody:{}\n\t异常信息:{}";
        // 处理body内的参数
        String requestBody = ServletUtil.getBody(request);
        // 处理堆栈错误信息
        String classLine = this.processStack(cause);
        // 打印自定义错误信息
        log.error(StrUtil.format(format, request.getRequestURL(), classLine
                , JSON.toJSONString(request.getParameterMap()), requestBody
                , ExceptionUtil.getMessage(cause)));
    }

    /**
     * 处理堆栈异常
     *
     * @param exception 异常信息
     * @return 异常堆栈信息
     */
    @SneakyThrows
    private String processStack(Exception exception) {
        // 获取异常的所有的堆栈信息
        StackTraceElement[] stackTraceElementArray = exception.getStackTrace();
        // 设置默认
        StackTraceElement stack = stackTraceElementArray[0];
        for (StackTraceElement item : stackTraceElementArray) {
            String className = item.getClassName();
            // base包抛出的类不需要
            if (!StrUtil.containsAny(className, "com.mzlalal.base", "$")
                    && StrUtil.contains(className, "com.mzlalal")) {
                stack = item;
                break;
            }
        }
        // 返回格式化信息
        return StrUtil.format("{}({}.java:{})", stack.getClassName()
                , Class.forName(stack.getClassName()).getSimpleName(), stack.getLineNumber());
    }

    /**
     * JSR 参数校验异常处理
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handleValidationException(HttpServletRequest request, ConstraintViolationException exception) {
        this.logExAndParams(request, exception);
        // 可能有多个条件校验失败 只返回第一条
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        ConstraintViolation<?> errorMsg = CollUtil.get(violations, 0);
        return Objects.nonNull(errorMsg) ? Result.failMsg(errorMsg.getMessage()) : Result.fail();
    }

    /**
     * JSR 参数校验异常处理
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(BindException.class)
    public Result<String> handleBindException(HttpServletRequest request, BindException exception) {
        this.logExAndParams(request, exception);
        // 可能有多个条件校验失败 只返回第一条
        List<FieldError> violations = exception.getFieldErrors();
        FieldError errorMsg = CollUtil.get(violations, 0);
        return Objects.nonNull(errorMsg) ? Result.failMsg(errorMsg.getDefaultMessage()) : Result.fail();
    }

    /**
     * 非法参数异常
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(HttpServletRequest request, Exception exception) {
        this.logExAndParams(request, exception);
        return Result.fail(ExceptionUtil.getMessage(exception));
    }

    /**
     * 全局异常
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(BoomException.class)
    public Result<String> handleBoomException(HttpServletRequest request, Exception exception) {
        String state = exception.getMessage();
        this.logCustomFormatMessage(request, exception);
        return GlobalResult.getEnum(state).result();
    }

    /**
     * 没有读取到datatype为json格式的参数
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<String> handleHttpMessageNotReadableException(HttpServletRequest request, Exception exception) {
        this.logExAndParams(request, exception);
        return GlobalResult.TYPE_NOT_JSON.result(ExceptionUtil.getMessage(exception));
    }

    /**
     * 没有读取到datatype为json格式的参数
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<String> handleHttpRequestMethodNotSupportedException(HttpServletRequest request, Exception exception) {
        this.logExAndParams(request, exception);
        return GlobalResult.REQ_NOT.result(ExceptionUtil.getMessage(exception));
    }

    /**
     * NPE 异常
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<String> handleNullPointerException(HttpServletRequest request, Exception exception) {
        this.logExAndParams(request, exception);
        return GlobalResult.SEVER_ERROR.result(ExceptionUtil.getMessage(exception));
    }

    /**
     * 数据重复
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, DuplicateKeyException.class})
    public Result<String> handleIntegrityConstraintViolationException(HttpServletRequest request, Exception exception) {
        this.logExAndParams(request, exception);
        return GlobalResult.DUPLICATE_KEY.result(ExceptionUtil.getMessage(exception));
    }

    /**
     * feign异常
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @SuppressWarnings("all")
    @ExceptionHandler(FeignException.class)
    public Result<String> handleFeignException(HttpServletRequest request, Exception exception) {
        String message = ExceptionUtil.getMessage(exception);
        String remoteMessage = StrUtil.subAfter(message, ": ", true);
        this.logExAndParams(request, exception);
        // 不是JSON返回系统异常
        if (!JSONValidator.from(remoteMessage).validate()) {
            return GlobalResult.FEIGN_SEVER_ERROR.result();
        }
        List<JSONObject> array = JSON.parseArray(remoteMessage, JSONObject.class);
        return array.get(0).toJavaObject(Result.class);
    }

    /**
     * 未知异常
     *
     * @param request   请求
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(HttpServletRequest request, Exception exception) {
        this.logExAndParams(request, exception);
        return GlobalResult.SEVER_ERROR.result(ExceptionUtil.getMessage(exception));
    }
}
