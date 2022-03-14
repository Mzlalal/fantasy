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
     * 请求找不到了，请稍后再试
     */
    REQ_NOT_FOUNT(404, "请求找不到了，请稍后再试"),
    /**
     * 手机未注册
     */
    MOBILE_NOT_FOUNT(1100, "手机未注册"),
    /**
     * 邮箱未注册
     */
    EMAIL_NOT_FOUNT(1101, "邮箱未注册"),
    /**
     * 密码不正确
     */
    PASSWORD_NOT_RIGHT(1102, "密码不正确"),
    /**
     * 验证码不正确
     */
    VALIDATE_CODE_NOT_RIGHT(1103, "验证码不正确"),
    /**
     * 邮箱格式不正确
     */
    EMAIL_NOT_CORRECT(1104, "邮箱格式不正确"),
    /**
     * 手机格式不正确
     */
    MOBILE_NOT_CORRECT(1105, "手机格式不正确"),
    /**
     * 账户被锁定
     */
    ACCOUNT_LOCKED(1106, "账户被锁定"),
    /**
     * 授权失败
     */
    OAUTH_FAIL(1400, "应用授权失败"),
    /**
     * 用户取消授权
     */
    OAUTH_CANCER(1401, "用户取消授权"),
    /**
     * 授权验证码不正确,请重新登录
     */
    OAUTH_CODE_NOT_CORRECT(1402, "授权验证码不正确，请重新登录"),
    /**
     * 非法的授权方式grantType
     */
    OAUTH_RESPONSE_TYPE_NOT_CORRECT(1403, "非法的授权方式(responseType)"),
    /**
     * 长时间未操作,请重新登录
     */
    LONG_TIME_NO_OPERATION(1404, "长时间未操作，请重新登录"),
    /**
     * 当前未登录
     */
    USER_NOT_LOGIN(1405, "当前未登录，请登录"),
    /**
     * 服务器繁忙
     */
    SEVER_ERROR(1500, "服务器繁忙，请稍后再试"),
    /**
     * 需dataType为application/json并传递参数
     */
    DATATYPE_NOT_JSON(1501, "需dataType为application/json并传递参数"),
    /**
     * 数据已存在
     */
    DUPLICATE_KEY(1600, "数据已存在"),
    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAIL(2100, "文件上传失败，请稍后再试"),
    /**
     * 扣除分数失败
     */
    SUB_SCORE_FAIL(2200, "扣除分数失败，可能发起人未加入房间"),
    /**
     * 增加分数失败
     */
    ADD_SCORE_FAIL(2201, "增加分数失败，可能接收人未加入房间"),
    /**
     * 扣除分数失败
     */
    SUB_PLAYER_STATUS_OFF(2203, "操作失败，发起人已经下桌"),
    /**
     * 扣除分数失败
     */
    ADD_PLAYER_STATUS_OFF(2204, "操作失败，接收人已经下桌"),
    /**
     * 房间已经存在
     */
    ROOM_EXIST(2205, "房间已经存在"),
    /**
     * 另一端登录
     */
    ANOTHER_LOGIN(2206, "另一端登录，连接断开"),
    /**
     * 非法的重复提醒周期(notifyType)
     */
    TODO_NOTIFY_TYPE_NOT_CORRECT(2300, "非法的重复提醒周期(notifyType)"),
    /**
     * 服务器繁忙
     */
    FEIGN_SEVER_ERROR(5000, "服务器繁忙，请稍后再试");

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
        return new RuntimeException("500");
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
