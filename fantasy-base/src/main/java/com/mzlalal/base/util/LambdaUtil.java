package com.mzlalal.base.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * lambda表达式工具类
 *
 * @author Mzlalal
 * @date 2022/2/22 13:42
 */
public class LambdaUtil {
    /**
     * 将list中的字段使用，连接成字符串,跳过null和空字符串
     *
     * @param list     集合
     * @param operator 操作方法
     * @param <O>      泛型
     * @return string
     */
    public static <O> String getFieldStr(List<O> list, Function<O, String> operator) {
        return getFieldStr(list, operator, ",");
    }

    /**
     * @param list     集合
     * @param operator 操作方法
     * @param split    切割符
     * @param <O>      泛型
     * @return string
     */
    public static <O> String getFieldStr(Collection<O> list, Function<O, String> operator, String split) {
        if (CollUtil.isEmpty(list)) {
            return "";
        }
        if (StrUtil.isBlank(split)) {
            split = ",";
        }
        StringBuilder sb = new StringBuilder();
        for (O item : list) {
            final String value = operator.apply(item);
            if (StrUtil.isBlank(value)) {
                continue;
            }
            sb.append(value).append(split);
        }
        if (sb.length() == 0) {
            return "";
        }
        return sb.delete(sb.length() - 1, sb.length()).toString();
    }
}
