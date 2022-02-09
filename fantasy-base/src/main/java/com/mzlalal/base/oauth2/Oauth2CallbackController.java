package com.mzlalal.base.oauth2;

import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.req.CreateTokenReq;
import com.mzlalal.base.entity.oauth2.req.OauthCallbackReq;
import com.mzlalal.base.entity.oauth2.vo.AccessToken;
import com.mzlalal.base.feign.oauth2.OauthFeignApi;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权回调
 *
 * @author Mzlalal
 * @date 2021/8/3 11:20
 */
@Slf4j
@Api(tags = "授权回调")
@Validated
@RestController
@RequestMapping("/oauth")
public class Oauth2CallbackController {

    private final OauthFeignApi oauthFeignApi;

    private final Oauth2Property oauth2Property;

    public Oauth2CallbackController(OauthFeignApi oauthFeignApi, Oauth2Property oauth2Property) {
        this.oauthFeignApi = oauthFeignApi;
        this.oauth2Property = oauth2Property;
    }

    /**
     * 回调
     *
     * @param oauthCallbackReq 授权码请求参数
     * @return AccessToken
     */
    @PostMapping("/callback")
    public Result<AccessToken> callback(@Validated @RequestBody OauthCallbackReq oauthCallbackReq) {
        // 根据tokenVO请求
        CreateTokenReq createTokenReq = CreateTokenReq.builder()
                .clientId(oauth2Property.getClientId())
                .clientSecret(oauth2Property.getClientSecret())
                .responseType(oauthCallbackReq.getResponseType())
                .authorizeCode(oauthCallbackReq.getCode())
                .build();
        return oauthFeignApi.createToken(createTokenReq);
    }
}
