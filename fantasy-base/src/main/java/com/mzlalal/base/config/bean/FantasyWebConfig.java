package com.mzlalal.base.config.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzlalal.base.interceptor.FantasyActionReportInterceptor;
import com.mzlalal.base.oauth2.Oauth2Property;
import com.mzlalal.base.oauth2.interceptor.Oauth2ServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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

        // 请求参数拦截器
        FantasyActionReportInterceptor fantasyActionReportInterceptor = new FantasyActionReportInterceptor();
        registry.addInterceptor(fantasyActionReportInterceptor)
                .addPathPatterns(fantasyActionReportInterceptor.getIncludePath())
                .excludePathPatterns(fantasyActionReportInterceptor.getExcludePath());
    }

    /**
     * 跨域请求拦截放行
     *
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(10000L);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    /**
     * 删除返回为空的数据
     *
     * @return ObjectMapper jackson配置对象映射
     */
    @Deprecated
    public ObjectMapper jacksonObjectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        return objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
