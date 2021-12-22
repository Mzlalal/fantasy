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
import javassist.ClassPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * request请求
     */
    @Autowired
    private HttpServletRequest request;

    /**
     * 打印系统异常和当前请求路径,请求参数
     *
     * @param cause 异常
     */
    protected void printExceptionAndParams(Exception cause) {
        // 打印原始报错
        log.error("", ExceptionUtil.getRootCause(cause));
        this.printCustomMessage(cause);
    }

    /**
     * 打印当前请求路径,请求参数
     *
     * @param cause 异常
     */
    protected void printCustomMessage(Exception cause) {
        // 输出格式
        String format = "\n\t请求路径:{}\n\t代码位置:{}\n\t请求参数:{}\n\t异常信息:{}";
        // 处理堆栈错误信息
        String classLine = this.printStack(cause);
        // 请求参数,非请求体
        String requestParam = JSON.toJSONString(ServletUtil.getParamMap(request));
        // 异常信息
        String exceptionMessage = ExceptionUtil.getMessage(cause);
        // 打印自定义错误信息
        log.error(StrUtil.format(format, request.getRequestURL(), classLine, requestParam, exceptionMessage));
    }

    /**
     * 处理堆栈异常
     *
     * @param exception 异常信息
     * @return 异常堆栈信息
     */
    @SneakyThrows
    private String printStack(Exception exception) {
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
                , ClassPool.getDefault().get(stack.getClassName()).getSimpleName(), stack.getLineNumber());
    }

    /**
     * JSR 参数校验异常处理
     *
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handleValidationException(ConstraintViolationException exception) {
        this.printCustomMessage(exception);
        // 可能有多个条件校验失败 只返回第一条
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        ConstraintViolation<?> errorMsg = CollUtil.get(violations, 0);
        return Objects.nonNull(errorMsg) ? Result.failMsg(errorMsg.getMessage()) : Result.fail();
    }

    /**
     * JSR 参数校验异常处理
     *
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(BindException.class)
    public Result<String> handleBindException(BindException exception) {
        this.printCustomMessage(exception);
        // 可能有多个条件校验失败 只返回第一条
        List<FieldError> violations = exception.getFieldErrors();
        FieldError errorMsg = CollUtil.get(violations, 0);
        return Objects.nonNull(errorMsg) ? Result.failMsg(errorMsg.getDefaultMessage()) : Result.fail();
    }

    /**
     * 非法参数异常
     *
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(Exception exception) {
        this.printExceptionAndParams(exception);
        return Result.fail(ExceptionUtil.getMessage(exception));
    }

    /**
     * 全局异常
     *
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(BoomException.class)
    public Result<String> handleBoomException(Exception exception) {
        String state = exception.getMessage();
        this.printCustomMessage(exception);
        return GlobalResult.getEnum(state).result();
    }

    /**
     * 没有读取到datatype为json格式的参数
     *
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<String> handleHttpMessageNotReadableException(Exception exception) {
        this.printExceptionAndParams(exception);
        return GlobalResult.TYPE_NOT_JSON.result(ExceptionUtil.getMessage(exception));
    }

    /**
     * 没有读取到datatype为json格式的参数
     *
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<String> handleHttpRequestMethodNotSupportedException(Exception exception) {
        this.printExceptionAndParams(exception);
        return GlobalResult.REQ_NOT.result(ExceptionUtil.getMessage(exception));
    }

    /**
     * NPE 异常
     *
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<String> handleNullPointerException(Exception exception) {
        this.printExceptionAndParams(exception);
        return GlobalResult.SEVER_ERROR.result(ExceptionUtil.getMessage(exception));
    }

    /**
     * 数据重复
     *
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, DuplicateKeyException.class})
    public Result<String> handleIntegrityConstraintViolationException(Exception exception) {
        this.printExceptionAndParams(exception);
        return GlobalResult.DUPLICATE_KEY.result(ExceptionUtil.getMessage(exception));
    }

    /**
     * feign异常
     *
     * @param exception 异常信息
     * @return Result
     */
    @SuppressWarnings("all")
    @ExceptionHandler(FeignException.class)
    public Result<String> handleFeignException(Exception exception) {
        String message = ExceptionUtil.getMessage(exception);
        String remoteMessage = StrUtil.subAfter(message, ": ", true);
        this.printExceptionAndParams(exception);
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
     * @param exception 异常信息
     * @return Result
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception exception) {
        this.printExceptionAndParams(exception);
        return GlobalResult.SEVER_ERROR.result(ExceptionUtil.getMessage(exception));
    }
}
