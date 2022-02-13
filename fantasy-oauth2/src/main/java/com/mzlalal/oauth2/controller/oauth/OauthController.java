package com.mzlalal.oauth2.controller.oauth;

import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.dto.ClientEntity;
import com.mzlalal.base.entity.oauth2.req.CheckVerifyCodeReq;
import com.mzlalal.base.entity.oauth2.req.CreateTokenReq;
import com.mzlalal.base.entity.oauth2.req.OauthReq;
import com.mzlalal.base.entity.oauth2.req.VerifyCodeReq;
import com.mzlalal.base.entity.oauth2.vo.AccessToken;
import com.mzlalal.base.entity.oauth2.vo.RedirectUriVo;
import com.mzlalal.base.feign.oauth2.OauthFeignApi;
import com.mzlalal.oauth2.config.oauth2.enums.GrantResponseEnum;
import com.mzlalal.oauth2.config.oauth2.enums.VerifyCodeProvideEnum;
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

    @Autowired
    public OauthController(ClientVerifyResponseTypeService clientVerifyResponseTypeService
            , StringRedisTemplate stringRedisTemplate) {
        this.clientVerifyResponseTypeService = clientVerifyResponseTypeService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Result<String> verifyCode(@Validated @RequestBody VerifyCodeReq verifyCodeReq) {
        // 授权方式
        String responseType = verifyCodeReq.getResponseType();
        clientVerifyResponseTypeService.verifyResponseType(verifyCodeReq.getClientId(), responseType);
        // 用户名
        String username = verifyCodeReq.getUsername();
        GrantResponseEnum.getEnum(responseType).verifyUsername(username);
        // 生成验证码
        return VerifyCodeProvideEnum.getEnum(responseType).createVerifyCode(username, verifyCodeReq.getClientId());
    }

    @Override
    public Result<String> checkVerifyCode(@Validated @RequestBody CheckVerifyCodeReq checkVerifyCodeReq) {
        // 用户名 固定为文本密码格式校验
        String username = checkVerifyCodeReq.getUsername();
        GrantResponseEnum.PASSWORD.verifyUsername(username);
        // 校验客户端
        String clientId = checkVerifyCodeReq.getClientId();
        clientVerifyResponseTypeService.verifyResponseType(clientId, GrantResponseEnum.PASSWORD.name());
        // 获取验证码
        String redisVal = stringRedisTemplate.opsForValue().
                get(GlobalConstant.clientIdPasswordCodeRedisKey(clientId, username));
        // 验证码为空直接返回失败 验证码相等则返回成功
        if (StrUtil.isNotBlank(redisVal) && StrUtil.equals(redisVal, checkVerifyCodeReq.getCode())) {
            return Result.ok();
        }
        // 返回失败
        return GlobalResult.VALIDATE_CODE_NOT_RIGHT.result();
    }

    @Override
    public Result<RedirectUriVo> authorize(@Validated @RequestBody OauthReq oauthReq) {
        // 授权方式
        String responseType = oauthReq.getResponseType();
        ClientEntity client = clientVerifyResponseTypeService.verifyResponseType(oauthReq.getClientId(), responseType);
        // 重定向地址为空,则设置默认的重新地址
        if (StrUtil.isBlank(oauthReq.getRedirectUri())) {
            oauthReq.setRedirectUri(client.getRedirectUri());
        }
        // 获取授权码
        return GrantResponseEnum.getEnum(responseType).createCallback(oauthReq);
    }

    @Override
    public Result<AccessToken> createToken(@Validated @RequestBody CreateTokenReq createTokenReq) {
        // 检查授权方式
        String responseType = createTokenReq.getResponseType();
        ClientEntity client = clientVerifyResponseTypeService.verifyResponseType(createTokenReq.getClientId(), responseType);
        // 获取授权码
        return GrantResponseEnum.getEnum(responseType).createAccessToken(createTokenReq, client);
    }
}
