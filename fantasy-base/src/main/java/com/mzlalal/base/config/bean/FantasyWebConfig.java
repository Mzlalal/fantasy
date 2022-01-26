package com.mzlalal.base.config.bean;

import com.mzlalal.base.interceptor.Oauth2ServerInterceptor;
import com.mzlalal.base.oauth2.Oauth2Property;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置webmvc
 *
 * @author Mzlalal
 * @date 2021/7/26 10:40
 */
@Configuration
public class FantasyWebConfig implements WebMvcConfigurer {

    private final RedisTemplate<String, Object> redisTemplate;

    private final Oauth2Property oauth2Property;

    public FantasyWebConfig(RedisTemplate<String, Object> redisTemplate, Oauth2Property oauth2Property) {
        this.redisTemplate = redisTemplate;
        this.oauth2Property = oauth2Property;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        Oauth2ServerInterceptor oauth2ServerInterceptor = new Oauth2ServerInterceptor(redisTemplate, oauth2Property);
        registry.addInterceptor(oauth2ServerInterceptor)
                .addPathPatterns(oauth2ServerInterceptor.getIncludePath())
                .excludePathPatterns(oauth2ServerInterceptor.getExcludePath());
    }
}
