package com.mzlalal.base.interceptor;

import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.oauth2.Oauth2Property;
import com.mzlalal.base.util.FilterUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截TOKEN
 * 如果TOKEN存在则存储用户信息
 * 如果TOKEN不存在则提示用户未登录
 *
 * @author Mzlalal
 * @date 2021/7/26 10:42
 */
public class Oauth2ServerInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;

    private final Oauth2Property oauth2Property;

    /**
     * 不需要登录即可访问的地址
     */
    private final String[] excludePath = new String[]{
            // oauth
            "/api/v1/oauth/logout", "/api/v1/oauth/create.token", "/api/v1/oauth/authorize"
            , "/api/v1/oauth/verify.code", "/api/v1/oauth/check.verify.code", "/oauth/callback"
            // notify message without token
            , "/api/v1/notify/**"
            // ignore controller
            , "/ignore/**"
            // doc swagger
            , "/doc.html", "/webjars/**", "/v2/api-docs", "/swagger-resources", "/swagger-resources/**"
            // static resource
            , "**/**.js", "**/**.css", "**/**.html", "**/**.ico", "/error", "/favicon.ico"};

    /**
     * 需要登录验证的网址
     */
    private final String[] includePath = new String[]{"/**"};

    public String[] getExcludePath() {
        return excludePath;
    }

    public String[] getIncludePath() {
        return includePath;
    }

    public Oauth2ServerInterceptor(RedisTemplate<String, Object> redisTemplate, Oauth2Property oauth2Property) {
        this.redisTemplate = redisTemplate;
        this.oauth2Property = oauth2Property;
    }

    @Override
    @SuppressWarnings("all")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取TOKEN
        String token = request.getHeader(GlobalConstant.F_AUTHORIZATION);
        // 未登录
        if (StrUtil.isBlank(token)) {
            FilterUtil.writeJson(response, GlobalResult.USER_NOT_LOGIN.result(oauth2Property.getLogin()));
            return false;
        }
        // 根据TOKEN获取redis用户信息
        UserEntity userEntity = (UserEntity) redisTemplate.opsForValue().get(GlobalConstant.tokenRedisKey(token));
        // 用户长时间未登录
        if (userEntity == null) {
            FilterUtil.writeJson(response, GlobalResult.LONG_TIME_NO_OPERATION.result(oauth2Property.getLogin()));
            return false;
        }
        // 设置上下文
        Oauth2Context.setContext(userEntity);
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 删除thread local的信息
        Oauth2Context.remove();
    }
}
