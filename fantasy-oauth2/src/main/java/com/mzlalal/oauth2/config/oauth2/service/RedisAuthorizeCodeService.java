package com.mzlalal.oauth2.config.oauth2.service;

import cn.hutool.core.lang.Assert;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 授权码服务
 * 存储授权码和用户信息至redis
 *
 * @author Mzlalal
 * @date 2021年7月28日 15:09:48
 **/
@Component
public class RedisAuthorizeCodeService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisAuthorizeCodeService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        Assert.notNull(redisTemplate, "redisTemplate不能为空");
    }

    /**
     * 根据authorizeCode存储用户信息
     *
     * @param clientKey  客户端ID
     * @param userEntity 用户信息
     * @return String 存储用户信息的redis authorizeCode授权码
     */
    public String store(String clientKey, UserEntity userEntity) {
        // 创建UUID
        String authorizeCode = GlobalConstant.clientKeyAuthCodeRedisKey(clientKey);
        // 存储
        redisTemplate.opsForValue().setIfAbsent(authorizeCode, userEntity, 15, TimeUnit.MINUTES);
        // 返回授权码
        return authorizeCode;
    }

    /**
     * 消费用户信息
     * 授权码只能用一次
     *
     * @param authorizeCode 授权码
     * @return userEntity 用户信息
     */
    public UserEntity consume(String authorizeCode) {
        // 获取用户信息
        UserEntity userEntity = (UserEntity) redisTemplate.opsForValue().get(authorizeCode);
        // 删除授权码 只能使用一次
        redisTemplate.delete(authorizeCode);
        // coded对应的用户信息不存在或者过期
        AssertUtil.notNull(userEntity, GlobalResult.OAUTH_CODE_NOT_CORRECT);
        return userEntity;
    }
}
