package com.mzlalal.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Mzlalal
 * @date 2022/5/29 21:45
 **/
@Getter
@AllArgsConstructor
public enum SubscribeStatusEnum {
    /**
     * 等待确认
     */
    SUBSCRIBE_CONFIRM(1, "订阅待确认"),
    /**
     * 订阅被拒绝
     */
    SUBSCRIBE_REFUSE(2, "订阅被拒绝"),
    /**
     * 已订阅
     */
    SUBSCRIBED(3, "已订阅"),
    /**
     * 取消订阅
     */
    UNSUBSCRIBE(4, "取消订阅");
    /**
     * 状态
     */
    private final Integer status;
    /**
     * 描述
     */
    private final String description;
}
