package com.mzlalal.base.config.feign;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 如果是feign请求
 * 自动转发token
 *
 * @author Mzlalal
 * @date 2021/7/5 16:44
 **/
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 从request获取token
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String authorization = (String) requestAttributes.getAttribute("F-Authorization", RequestAttributes.SCOPE_REQUEST);
        // 设置头部,服务之间需要验证
        if (StrUtil.isNotBlank(authorization)) {
            requestTemplate.header("F-Authorization", authorization);
        }
    }
}
