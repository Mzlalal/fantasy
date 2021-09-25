package com.mzlalal.base.config.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private RedisTemplate<String, Object> redisTemplate;

    private final Oauth2Property oauth2Property;

    public FantasyWebConfig(RedisTemplate<String, Object> redisTemplate, Oauth2Property oauth2Property) {
        this.redisTemplate = redisTemplate;
        this.oauth2Property = oauth2Property;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 不需要登录即可访问的地址
        String[] excludePath = new String[]{"/oauth/**", "/doc.html", "/webjars/**", "/v2/api-docs",
                "/swagger-resources", "/swagger-resources/**", "**/**.css", "**/**.js", "**/**.html", "**/**.ico"};
        // 登录拦截器
        registry.addInterceptor(new Oauth2ServerInterceptor(redisTemplate, oauth2Property))
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
    }

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
