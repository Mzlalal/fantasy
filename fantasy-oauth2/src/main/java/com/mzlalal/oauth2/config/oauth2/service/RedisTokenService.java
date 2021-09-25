package com.mzlalal.oauth2.config.oauth2.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oauth2.AccessToken;
import com.mzlalal.base.entity.oauth2.ClientEntity;
import com.mzlalal.base.entity.oauth2.UserEntity;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * redis存储token和用户信息
 *
 * @author Mzlalal88
 * @date 2021/7/28 16:54
 */
@Component
public class RedisTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisTokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        Assert.notNull(redisTemplate, "redisTemplate不能为空");
    }

    /**
     * 创建AccessToken
     *
     * @param userEntity   用户试题
     * @param clientEntity 客户端
     * @return AccessToken
     */
    public AccessToken createAccessToken(UserEntity userEntity, ClientEntity clientEntity) {
        // 如果存在token则返回旧的
        if (StrUtil.isNotBlank(userEntity.getAccessToken())) {
            // 如果存在redis 则返回旧的信息
            Boolean bool = redisTemplate.hasKey(GlobalConstant.tokenRedisKey(userEntity.getAccessToken()));
            if (BooleanUtil.isTrue(bool)) {
                return BeanUtil.copyProperties(userEntity, AccessToken.class);
            }
        }
        // 如果不存在则创建TOKEN并存储在redis
        AccessToken accessToken = AccessToken.builder()
                .accessToken(IdUtil.fastSimpleUUID())
                .refreshToken(IdUtil.fastSimpleUUID())
                .expireAt(DateUtil.offsetSecond(new Date(), clientEntity.getAccessTokenTime()))
                .build();
        // 设置TOKEN
        Boolean tokenBool = redisTemplate.opsForValue().setIfAbsent(GlobalConstant.tokenRedisKey(accessToken.getAccessToken())
                , userEntity, clientEntity.getAccessTokenTime(), TimeUnit.SECONDS);
        // 设置失败
        AssertUtil.isTrue(BooleanUtil.isTrue(tokenBool), GlobalResult.OAUTH_FAIL);
        // 设置刷新TOKEN
        Boolean refreshBool = redisTemplate.opsForValue().setIfAbsent(GlobalConstant.tokenRedisKey(accessToken.getRefreshToken())
                , "", clientEntity.getRefreshTokenTime(), TimeUnit.SECONDS);
        // 设置失败
        AssertUtil.isTrue(BooleanUtil.isTrue(refreshBool), GlobalResult.OAUTH_FAIL);
        // 更新用户token
        BeanUtil.copyProperties(accessToken, userEntity);
        SpringUtil.getBean(UserService.class)
                .updateAccessTokenById(userEntity.getId(), userEntity);
        return accessToken;
    }
}
