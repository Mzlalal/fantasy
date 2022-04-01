package com.mzlalal.oauth2.controller;

import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.feign.oauth2.TokenFeignApi;
import com.mzlalal.base.oauth2.Oauth2Context;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TOKEN令牌操作Controller
 *
 * @author Mzlalal
 * @date 2022/2/8 11:20
 */
@Slf4j
@Api(tags = "令牌")
@Validated
@RestController
@RequestMapping("/api/v1/oauth2/token")
public class TokenController implements TokenFeignApi {

    /**
     * string=>对象 redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public TokenController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Result<UserEntity> checkToken(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token) {
        return Result.ok(Oauth2Context.get());
    }

    @Override
    public Result<Void> logout(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token) {
        redisTemplate.delete(GlobalConstant.userToken(token));
        return Result.ok();
    }
}
