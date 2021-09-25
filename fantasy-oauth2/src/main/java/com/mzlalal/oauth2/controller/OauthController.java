package com.mzlalal.oauth2.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.ClientEntity;
import com.mzlalal.base.entity.oauth2.vo.OauthVo;
import com.mzlalal.base.entity.oauth2.vo.TokenVo;
import com.mzlalal.base.feign.oauth2.OauthFeignApi;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.oauth2.config.oauth2.enums.GrantResponseEnum;
import com.mzlalal.oauth2.service.ClientService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
@RequestMapping("/oauth")
public class OauthController implements OauthFeignApi {

    private final ClientService clientService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public OauthController(ClientService clientService, BCryptPasswordEncoder passwordEncoder, RedisTemplate<String, Object> redisTemplate) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Result<BaseEntity> authorize(OauthVo oauthVo) {
        // 查询客户端
        ClientEntity client = clientService.getOneByClientId(oauthVo.getClientId());
        AssertUtil.notNull(client, GlobalResult.OAUTH_FAIL);
        // 获取授权类型
        String[] grantTypeArray = StrUtil.splitToArray(client.getResponseType(), ",");
        AssertUtil.isTrue(ArrayUtil.contains(grantTypeArray, oauthVo.getGrantType()), GlobalResult.OAUTH_RESPONSE_TYPE_NOT_CORRECT);
        // 重定向地址为空,则设置默认的重新地址
        if (StrUtil.isBlank(oauthVo.getRedirectUri())) {
            oauthVo.setRedirectUri(client.getRedirectUri());
        }
        // 获取授权码
        return GrantResponseEnum.getEnum(oauthVo.getGrantType()).processGrant(oauthVo);
    }

    @Override
    public Result<BaseEntity> token(@RequestBody TokenVo tokenVo) {
        // 查询客户端
        ClientEntity client = clientService.getOneByClientId(tokenVo.getClientId());
        // 验证
        AssertUtil.notNull(client, GlobalResult.OAUTH_FAIL);
        AssertUtil.isTrue(passwordEncoder.matches(tokenVo.getClientSecret(), client.getClientSecret()), GlobalResult.OAUTH_FAIL);
        AssertUtil.isTrue(StrUtil.contains(client.getResponseType(), tokenVo.getResponseType()), GlobalResult.OAUTH_RESPONSE_TYPE_NOT_CORRECT);
        OauthVo oauthVo = BeanUtil.copyProperties(tokenVo, OauthVo.class);
        // 获取授权码
        return GrantResponseEnum.getEnum(tokenVo.getResponseType()).processToken(oauthVo, client);
    }

    @Override
    public Result<Void> logout(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token) {
        redisTemplate.delete(GlobalConstant.tokenRedisKey(token));
        return Result.ok();
    }
}
