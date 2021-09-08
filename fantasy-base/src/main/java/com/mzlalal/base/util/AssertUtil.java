package com.mzlalal.base.util;

import cn.hutool.core.lang.Assert;
import com.mzlalal.base.common.GlobalResult;

/**
 * 断言
 * 自定义全局错误码
 * 继承 hutool 断言
 *
 * @author Mzlalal88
 * @date 2021/8/2 16:04
 */
public class AssertUtil extends Assert {

    /**
     * 为false则抛出异常
     *
     * @param flag       判断
     * @param resultCode 全局状态码
     */
    public static void isTrue(boolean flag, GlobalResult resultCode) {
        if (!flag) {
            throw resultCode.boom();
        }
    }

    /**
     * 对象为空则抛出异常
     *
     * @param obj        判断
     * @param resultCode 全局状态码
     */
    public static void notNull(Object obj, GlobalResult resultCode) {
        if (obj == null) {
            throw resultCode.boom();
        }
    }
}
