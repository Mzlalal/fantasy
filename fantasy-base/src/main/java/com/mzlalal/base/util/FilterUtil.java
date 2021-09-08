package com.mzlalal.base.util;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

/**
 * 过滤器处理JSON
 *
 * @author Mzlalal
 * @date 2021/7/5 17:48
 **/
@Slf4j
public class FilterUtil {

    /**
     * 返回JSON结果
     *
     * @param response 响应
     * @param object   输出对象
     */
    public static void writeJson(HttpServletResponse response, Object object) {
        ServletUtil.write(response, JSON.toJSONString(object), "application/json; charset=UTF-8");
    }
}
