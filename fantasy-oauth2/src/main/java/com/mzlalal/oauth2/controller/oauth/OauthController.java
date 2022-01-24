package com.mzlalal.oauth2.controller.oauth;

import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.ClientEntity;
import com.mzlalal.base.entity.oauth2.vo.CheckVerifyCodeVo;
import com.mzlalal.base.entity.oauth2.vo.OauthVo;
import com.mzlalal.base.entity.oauth2.vo.VerifyCodeVo;
import com.mzlalal.base.feign.oauth2.OauthFeignApi;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.oauth2.config.oauth2.enums.GrantResponseEnum;
import com.mzlalal.oauth2.config.oauth2.enums.VerifyCodeProvideEnum;
import com.mzlalal.oauth2.service.impl.ClientVerifyResponseTypeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权Controller
 *
 * @author Mzlalal88
 * @date 2021/7/28 14:26
 */
@Slf4j
@Api(tags = "登录授权")
@Validated
@RestController
@RequestMapping("/api/v1/oauth")
public class OauthController implements OauthFeignApi {
    /**
     * 客户端
     */
    private final ClientVerifyResponseTypeService clientVerifyResponseTypeService;
    /**
     * 密码加密
     */
    private final BCryptPasswordEncoder passwordEncoder;
    /**
     * string=>对象 redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * string redis操作模板
     */
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public OauthController(ClientVerifyResponseTypeService clientVerifyResponseTypeService
            , BCryptPasswordEncoder passwordEncoder, RedisTemplate<String, Object> redisTemplate
            , StringRedisTemplate stringRedisTemplate) {
        this.clientVerifyResponseTypeService = clientVerifyResponseTypeService;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Result<String> verifyCode(@Validated @RequestBody VerifyCodeVo verifyCodeVo) {
        // 检查授权方式
        String responseType = verifyCodeVo.getResponseType();
        clientVerifyResponseTypeService.verifyResponseType(verifyCodeVo.getClientId(), responseType);
        // 发送验证码
        return VerifyCodeProvideEnum.getEnum(responseType).generateVerifyCode(verifyCodeVo.getUsername());
    }

    @Override
    public Result<String> checkVerifyCode(@Validated @RequestBody CheckVerifyCodeVo checkVerifyCodeVo) {
        String username = checkVerifyCodeVo.getUsername();
        // 获取验证码
        String redisVal = stringRedisTemplate.opsForValue().
                get(GlobalConstant.passwordCodeRedisKey(username));
        // 验证码为空直接返回失败 验证码相等则返回成功
        if (StrUtil.isNotBlank(redisVal) && StrUtil.equals(redisVal, checkVerifyCodeVo.getCode())) {
            return Result.ok();
        }
        // 返回失败
        Result<String> base64Result = VerifyCodeProvideEnum.PASSWORD.generateVerifyCode(username);
        return Result.failMsg(base64Result.getData());
    }

    @Override
    public Result<BaseEntity> authorize(@Validated @RequestBody OauthVo oauthVo) {
        // 检查授权方式
        ClientEntity client = clientVerifyResponseTypeService.verifyResponseType(oauthVo.getClientId(), oauthVo.getResponseType());
        // 重定向地址为空,则设置默认的重新地址
        if (StrUtil.isBlank(oauthVo.getRedirectUri())) {
            oauthVo.setRedirectUri(client.getRedirectUri());
        }
        // 获取授权码
        return GrantResponseEnum.getEnum(oauthVo.getResponseType()).processGrant(oauthVo);
    }

    @Override
    public Result<BaseEntity> createToken(@Validated @RequestBody OauthVo oauthVo) {
        // 检查授权方式
        String responseType = oauthVo.getResponseType();
        ClientEntity client = clientVerifyResponseTypeService.verifyResponseType(oauthVo.getClientId(), responseType);
        // 私匙
        String clientSecret = client.getClientSecret();
        AssertUtil.isTrue(passwordEncoder.matches(oauthVo.getClientSecret(), clientSecret), GlobalResult.OAUTH_FAIL);
        // 获取授权码
        return GrantResponseEnum.getEnum(responseType).processToken(oauthVo, client);
    }

    @Override
    public Result<BaseEntity> checkToken(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token) {
        return Result.ok(Oauth2Context.get());
    }

    @Override
    public Result<Void> logout(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token) {
        redisTemplate.delete(GlobalConstant.tokenRedisKey(token));
        return Result.ok();
    }
}
