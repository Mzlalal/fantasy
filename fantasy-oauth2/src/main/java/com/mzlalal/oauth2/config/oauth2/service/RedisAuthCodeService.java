package com.mzlalal.oauth2.config.oauth2.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oauth2.UserEntity;
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
public class RedisAuthCodeService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisAuthCodeService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        Assert.notNull(redisTemplate, "redisTemplate不能为空");
    }

    /**
     * 存储用户信息
     *
     * @param userEntity 用户信息
     */
    public String store(UserEntity userEntity) {
        // 创建UUID
        String authCode = IdUtil.fastSimpleUUID();
        // 存储
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(authCode, userEntity, 15, TimeUnit.MINUTES);
        // 如果不存在抛出异常
        AssertUtil.isTrue(BooleanUtil.isTrue(absent), GlobalResult.OAUTH_FAIL);
        // 返回授权码
        return authCode;
    }

    /**
     * 消费用户信息
     * 授权码只能用一次
     *
     * @param code 授权码
     * @return userEntity 用户信息
     */
    public UserEntity consume(String code) {
        // 获取用户信息
        UserEntity userEntity = (UserEntity) redisTemplate.opsForValue().get(code);
        // 删除授权码 只能使用一次
        redisTemplate.delete(code);
        // coded对应的用户信息不存在或者过期
        AssertUtil.notNull(userEntity, GlobalResult.OAUTH_CODE_NOT_CORRECT);
        return userEntity;
    }
}
