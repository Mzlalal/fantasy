package com.mzlalal.base.oauth2;

import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.AccessToken;
import com.mzlalal.base.entity.oauth2.vo.TokenVo;
import com.mzlalal.base.feign.oauth2.OauthFeignApi;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权回调
 *
 * @author Mzlalal88
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
     * @param code 授权码
     * @return AccessToken
     */
    @RequestMapping("/callback")
    public AccessToken callback(String code) {
        // 根据tokenVO请求
        TokenVo tokenVo = TokenVo.builder()
                .clientId(oauth2Property.getClientId())
                .clientSecret(oauth2Property.getClientSecret())
                .type(GlobalConstant.VALIDATE_CODE)
                .field(code)
                .build();
        Result<BaseEntity> result = oauthFeignApi.token(tokenVo);
        return (AccessToken) result.getData();
    }
}
