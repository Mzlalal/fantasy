package com.mzlalal.base.config.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.mzlalal.base.oauth2.Oauth2Property;
import com.mzlalal.base.oauth2.interceptor.Oauth2ServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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

    public FantasyWebConfig(Oauth2Property oauth2Property) {
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

    /**
     * redisTemplate存储对象
     *
     * @param redisConnectionFactory redis连接工厂
     * @return RedisTemplate<String, Object>
     */
    @Bean("redisTemplateStoreJson")
    public RedisTemplate<String, Object> redisTemplateStoreJson(RedisConnectionFactory redisConnectionFactory) {
        this.redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jacksonSerialize = new Jackson2JsonRedisSerializer<>(Object.class);
        // 对象映射
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jacksonSerialize.setObjectMapper(om);

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jacksonSerialize);
        // 设置hash key 和value序列化模式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jacksonSerialize);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
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
    public ObjectMapper jacksonObjectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        return objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
