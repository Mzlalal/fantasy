package com.mzlalal.oauth2.config.oauth2.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.oauth2.dto.ClientEntity;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.entity.oauth2.vo.AccessToken;
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
 * @author Mzlalal
 * @date 2021/7/28 16:54
 */
@Component
public class RedisTokenService {

    /**
     * redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * 用户service
     */
    private final UserService userService;

    @Autowired
    public RedisTokenService(RedisTemplate<String, Object> redisTemplate, UserService userService) {
        this.redisTemplate = redisTemplate;
        Assert.notNull(redisTemplate, "redisTemplate不能为空");
        this.userService = userService;
        Assert.notNull(userService, "userService不能为空");
    }

    /**
     * 创建AccessToken
     *
     * @param userEntity   用户试题
     * @param clientEntity 客户端
     * @return AccessToken
     */
    public AccessToken createAccessToken(UserEntity userEntity, ClientEntity clientEntity) {
        String oldAccessToken = userEntity.getAccessToken();
        String oldAccessTokenRedisKey = GlobalConstant.tokenRedisKey(oldAccessToken);
        // 如果存在token则返回旧的
        if (StrUtil.isNotBlank(oldAccessToken)) {
            // 如果存在redis 则返回旧的信息
            UserEntity oldUserEntity = (UserEntity) redisTemplate.opsForValue().get(oldAccessTokenRedisKey);
            if (userEntity.equals(oldUserEntity)) {
                return BeanUtil.copyProperties(userEntity, AccessToken.class);
            }
        }
        // 删除旧的用户信息
        if (StrUtil.isNotBlank(oldAccessToken)) {
            redisTemplate.delete(oldAccessTokenRedisKey);
        }
        // 删除旧的刷新TOKEN
        if (StrUtil.isNotBlank(userEntity.getRefreshToken())) {
            redisTemplate.delete(GlobalConstant.tokenRedisKey(userEntity.getRefreshToken()));
        }
        // 如果不存在则创建TOKEN并存储在redis
        AccessToken accessToken = AccessToken.builder()
                .accessToken(IdUtil.fastSimpleUUID())
                .refreshToken(IdUtil.fastSimpleUUID())
                .expireAt(DateUtil.offsetSecond(new Date(), clientEntity.getAccessTokenTime()))
                .build();
        // 更新用户token
        BeanUtil.copyProperties(accessToken, userEntity);
        AssertUtil.isTrue(userService.updateAccessTokenById(userEntity.getId(), userEntity)
                , "更新用户登录信息失败,可能是用户不存在");
        // 更新用户信息并缓存
        userEntity = userService.getById(userEntity.getId());
        // 设置TOKEN
        redisTemplate.opsForValue().setIfAbsent(GlobalConstant.tokenRedisKey(accessToken.getAccessToken())
                , userEntity, clientEntity.getAccessTokenTime(), TimeUnit.SECONDS);
        // 设置刷新TOKEN
        redisTemplate.opsForValue().setIfAbsent(GlobalConstant.tokenRedisKey(accessToken.getRefreshToken())
                , "", clientEntity.getRefreshTokenTime(), TimeUnit.SECONDS);
        return accessToken;
    }
}
