package com.mzlalal.base.common;

import cn.hutool.core.lang.Validator;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.exception.BoomException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 全局状态码
 *
 * @author Mzlalal
 * @date 2021/6/5 20:15
 **/
@Slf4j
@Getter
public enum GlobalResult {
    /**
     * 授权失败
     */
    SUCCESS(200, "操作成功"),
    /**
     * 授权失败
     */
    OAUTH_FAIL(401, "应用授权失败"),
    /**
     * 用户取消授权
     */
    OAUTH_CANCER(402, "用户取消授权"),
    /**
     * 服务器繁忙
     */
    REQ_NOT_FOUNT(404, "请求找不到了，请稍后再试"),
    /**
     * dataType不是json
     */
    TYPE_NOT_JSON(410, "需dataType为application/json并传递参数"),
    /**
     * 长时间未操作,请重新登录
     */
    LONG_TIME_NO_OPERATION(411, "长时间未操作，请重新登录"),
    /**
     * 授权验证码不正确,请重新登录
     */
    OAUTH_CODE_NOT_CORRECT(412, "授权验证码不正确，请重新登录"),
    /**
     * 非法的授权方式grantType
     */
    OAUTH_RESPONSE_TYPE_NOT_CORRECT(413, "非法的授权方式(responseType)"),
    /**
     * 非法的授权方式validateType
     */
    @Deprecated
    OAUTH_VALIDATE_TYPE_NOT_CORRECT(414, "非法的授权方式(validateType)"),
    /**
     * 授权方式为空
     */
    RESPONSE_TYPE_NOT_CORRECT(415, "授权方式为空(responseType)"),
    /**
     * 服务器繁忙
     */
    SEVER_ERROR(500, "服务器繁忙，请稍后再试"),
    /**
     * 服务器繁忙
     */
    FEIGN_SEVER_ERROR(5000, "服务器繁忙，请稍后再试"),
    /**
     * 当前请求不支持当前METHOD访问
     */
    REQ_NOT(501, "当前请求不支持当前METHOD访问"),
    /**
     * 数据已存在
     */
    DUPLICATE_KEY(600, "数据已存在"),
    /**
     * 当前未登录
     */
    USER_NOT_LOGIN(1000, "当前未登录"),
    /**
     * 手机未注册
     */
    MOBILE_NOT_FOUNT(1001, "手机未注册"),
    /**
     * 密码不正确
     */
    PASSWORD_NOT_RIGHT(1002, "密码不正确"),
    /**
     * 验证码不正确
     */
    VALIDATE_CODE_NOT_RIGHT(1003, "验证码不正确"),
    /**
     * 账户被锁定
     */
    ACCOUNT_LOCKED(1004, "账户被锁定"),
    /**
     * 邮箱格式不正确
     */
    EMAIL_NOT_CORRECT(1005, "邮箱格式不正确"),
    /**
     * 邮箱未注册
     */
    EMAIL_NOT_FOUNT(1006, "邮箱未注册"),
    /**
     * 手机格式不正确
     */
    MOBILE_NOT_CORRECT(1007, "手机格式不正确"),
    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAIL(1100, "文件上传失败，请稍后再试");

    /**
     * 状态
     */
    private final Integer state;

    /**
     * 信息
     */
    private final String message;

    GlobalResult(Integer state, String message) {
        this.state = state;
        this.message = message;
    }

    /**
     * 根据参数获取模板信息
     *
     * @param data 数据
     * @param <T>  泛型
     * @return Result
     */
    public <T> Result<T> result(T data) {
        return Result.state(this.state, this.message, data);
    }

    /**
     * 根据参数获取模板信息
     *
     * @return Result
     */
    public <T> Result<T> result() {
        return Result.state(this.state, this.message);
    }

    /**
     * 抛出异常
     *
     * @return RuntimeException 异常
     */
    public RuntimeException boom() {
        return this.boom(BoomException.class);
    }

    /**
     * 抛出异常
     *
     * @param e 异常类
     * @return RuntimeException 异常
     */
    public RuntimeException boom(Class<? extends RuntimeException> e) {
        try {
            Constructor<? extends RuntimeException> constructor = e.getConstructor(String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(this.getState().toString());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            log.error("GlobalResultCode抛出异常错误", exception);
        }
        return new RuntimeException();
    }

    /**
     * 根据状态码获取返回结果
     *
     * @param state 状态码 string
     * @return Result 返回结果
     */
    public static GlobalResult getEnum(String state) {
        return Validator.isNumber(state) ? getEnum(Integer.valueOf(state)) : SEVER_ERROR;
    }

    /**
     * 根据状态码获取返回结果
     *
     * @param state 状态码 integer
     * @return Result 返回结果
     */
    public static GlobalResult getEnum(Integer state) {
        for (GlobalResult item : GlobalResult.values()) {
            if (item.getState().equals(state)) {
                return item;
            }
        }
        return SEVER_ERROR;
    }
}
