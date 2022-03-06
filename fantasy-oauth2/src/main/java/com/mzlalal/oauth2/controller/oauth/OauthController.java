package com.mzlalal.oauth2.controller.oauth;

import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.dto.ClientEntity;
import com.mzlalal.base.entity.oauth2.req.*;
import com.mzlalal.base.entity.oauth2.vo.AccessToken;
import com.mzlalal.base.entity.oauth2.vo.RedirectUriVo;
import com.mzlalal.base.feign.oauth2.OauthFeignApi;
import com.mzlalal.oauth2.config.oauth2.enums.GrantResponseEnum;
import com.mzlalal.oauth2.config.oauth2.enums.VerifyCodeProvideEnum;
import com.mzlalal.oauth2.config.oauth2.service.RedisTokenService;
import com.mzlalal.oauth2.service.impl.ClientVerifyResponseTypeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权Controller
 *
 * @author Mzlalal
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
     * string redis操作模板
     */
    private final StringRedisTemplate stringRedisTemplate;
    /**
     * redis token服务
     */
    private final RedisTokenService redisTokenService;

    @Autowired
    public OauthController(ClientVerifyResponseTypeService clientVerifyResponseTypeService
            , StringRedisTemplate stringRedisTemplate, RedisTokenService redisTokenService) {
        this.clientVerifyResponseTypeService = clientVerifyResponseTypeService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTokenService = redisTokenService;
    }

    @Override
    public Result<String> verifyCode(@Validated @RequestBody VerifyCodeReq req) {
        // 授权方式
        String responseType = req.getResponseType();
        clientVerifyResponseTypeService.verifyResponseType(req.getClientKey(), responseType);
        // 用户名
        String username = req.getUsername();
        GrantResponseEnum.getEnum(responseType).verifyUsername(username);
        // 生成验证码
        return VerifyCodeProvideEnum.getEnum(responseType).createVerifyCode(username, req.getClientKey());
    }

    @Override
    public Result<String> checkVerifyCode(@Validated @RequestBody CheckVerifyCodeReq req) {
        // 用户名 固定为文本密码格式校验
        String username = req.getUsername();
        GrantResponseEnum.PASSWORD.verifyUsername(username);
        // 校验客户端
        String clientKey = req.getClientKey();
        clientVerifyResponseTypeService.verifyResponseType(clientKey, GrantResponseEnum.PASSWORD.name());
        // 获取验证码
        String redisVal = stringRedisTemplate.opsForValue().
                get(GlobalConstant.clientKeyPasswordCodeRedisKey(clientKey, username));
        // 验证码为空直接返回失败 验证码相等则返回成功
        if (StrUtil.isNotBlank(redisVal) && StrUtil.equals(redisVal, req.getCode())) {
            return Result.ok();
        }
        // 返回失败
        return GlobalResult.VALIDATE_CODE_NOT_RIGHT.result();
    }

    @Override
    public Result<RedirectUriVo> authorize(@Validated @RequestBody OauthReq req) {
        // 授权方式
        String responseType = req.getResponseType();
        ClientEntity client = clientVerifyResponseTypeService.verifyResponseType(req.getClientKey(), responseType);
        // 重定向地址为空,则设置默认的重新地址
        if (StrUtil.isBlank(req.getRedirectUri())) {
            req.setRedirectUri(client.getRedirectUri());
        }
        // 首页地址为空,则设置默认的首页地址
        if (StrUtil.isBlank(req.getIndexUri())) {
            req.setIndexUri(client.getIndexUri());
        }
        // 获取授权码
        return GrantResponseEnum.getEnum(responseType).createCallback(req);
    }

    @Override
    public Result<AccessToken> createToken(@Validated @RequestBody CreateTokenReq req) {
        // 检查授权方式
        String responseType = req.getResponseType();
        ClientEntity client = clientVerifyResponseTypeService.verifyResponseType(req.getClientKey(), responseType);
        // 获取授权码
        return GrantResponseEnum.getEnum(responseType).createAccessToken(req, client);
    }

    @Override
    public Result<AccessToken> refreshToken(@Validated @RequestBody RefreshTokenReq req) {
        return redisTokenService.refreshAccessToken(req);
    }
}
