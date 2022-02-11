package com.mzlalal.base.entity.global;

import cn.hutool.core.date.DateUtil;
import com.mzlalal.base.util.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回结果
 *
 * @author Mzlalal
 * @date 2021/4/22 11:28
 **/
@Data
@Builder
@ApiModel("API返回结果")
@NoArgsConstructor
@AllArgsConstructor
public class WsResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 正确状态
     */
    private static final Integer STATE_OK = 200;
    /**
     * 错误状态
     */
    private static final Integer STATE_FAIL = 500;

    @ApiModelProperty(value = "返回状态")
    private Integer state;

    @ApiModelProperty(value = "返回信息")
    private String msg;

    @ApiModelProperty("返回数据")
    private T data;

    @Builder.Default
    @ApiModelProperty("分页数据")
    private Page<T> pageInfo = Page.empty();

    @Builder.Default
    @ApiModelProperty("时间")
    private String time = DateUtil.now();

    @ApiModelProperty("发送人")
    private String from;

    public static <T> WsResult<T> okMsg(String msg) {
        return WsResult.<T>builder().state(STATE_OK).msg(msg).build();
    }

    public static <T> WsResult<T> failMsg(String msg) {
        return WsResult.<T>builder().state(STATE_FAIL).msg(msg).build();
    }
}
