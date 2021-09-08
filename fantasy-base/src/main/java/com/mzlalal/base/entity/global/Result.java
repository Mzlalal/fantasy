package com.mzlalal.base.entity.global;

import com.mzlalal.base.util.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.io.Serializable;

/**
 * 返回结果
 *
 * @author Mzlalal
 * @date 2021/4/22 11:28
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("返回结果包装类")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -7552064024010817950L;

    /**
     * 正确状态
     */
    private static final Integer STATE_OK = 200;
    /**
     * 错误状态
     */
    private static final Integer STATE_FAIL = 500;
    /**
     * 成功信息
     */
    private static final String STATE_OK_MSG = "操作成功";
    /**
     * 错误信息
     */
    private static final String STATE_FAIL_MSG = "操作失败";

    @ApiModelProperty(value = "返回状态", example = "200")
    private Integer state;

    @ApiModelProperty(value = "返回信息,用于弹窗提示使用", example = STATE_OK_MSG)
    private String msg;

    @ApiModelProperty("返回数据")
    private T data;

    @ApiModelProperty("分页数据")
    private Page<T> pageInfo;

    public static <T> Result<T> ok() {
        return Result.<T>builder().state(STATE_OK).msg(STATE_OK_MSG).build();
    }

    public static <T> Result<T> ok(T entity) {
        return Result.<T>builder().state(STATE_OK).msg(STATE_OK_MSG).data(entity).build();
    }

    public static <T> Result<T> okMsg(String msg) {
        return Result.<T>builder().state(STATE_OK).msg(msg).build();
    }

    public static <T> Result<T> ok(String msg, T entity) {
        return Result.<T>builder().state(STATE_OK).msg(msg).data(entity).build();
    }

    public static <T> Result<T> ok(Page<T> page) {
        return Result.<T>builder().state(STATE_OK).msg(STATE_OK_MSG).pageInfo(page).build();
    }

    public static <T> Result<T> fail() {
        return Result.<T>builder().state(STATE_FAIL).msg(STATE_OK_MSG).build();
    }

    public static <T> Result<T> fail(T entity) {
        return Result.<T>builder().state(STATE_FAIL).msg(STATE_OK_MSG).data(entity).build();
    }

    public static <T> Result<T> failMsg(String msg) {
        return Result.<T>builder().state(STATE_FAIL).msg(msg).build();
    }

    public static <T> Result<T> fail(String msg, T entity) {
        return Result.<T>builder().state(STATE_FAIL).msg(msg).data(entity).build();
    }

    public static <T> Result<T> state(int state, String msg) {
        return Result.<T>builder().state(STATE_OK).msg(msg).build();
    }

    public static <T> Result<T> state(int state, String msg, T entity) {
        return Result.<T>builder().state(STATE_OK).msg(msg).data(entity).build();
    }

    /**
     * 是否请求成功
     *
     * @return true 成功 false 失败
     */
    @Transient
    public boolean isOk() {
        return STATE_OK.equals(this.state);
    }
}
