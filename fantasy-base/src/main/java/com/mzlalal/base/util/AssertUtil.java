package com.mzlalal.base.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import com.mzlalal.base.common.GlobalResult;

import java.util.Collection;

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

    /**
     * 集合不包含则抛出异常
     *
     * @param collection 字符串集合
     * @param str        字符串
     * @param resultCode 全局状态码
     */
    public static <T> void collNotContains(Collection<T> collection, T str, GlobalResult resultCode) {
        AssertUtil.isTrue(CollUtil.contains(collection, str), resultCode);
    }

    /**
     * 数组不包含则抛出异常
     *
     * @param array      字符串数组
     * @param str        字符串
     * @param resultCode 全局状态码
     */
    public static <T> void arrayNotContains(T[] array, T str, GlobalResult resultCode) {
        AssertUtil.isTrue(ArrayUtil.contains(array, str), resultCode);
    }
}
