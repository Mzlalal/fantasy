package com.mzlalal.oauth2.controller.oauth;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.notify.AccountAuthorizeCodeEntity;
import com.mzlalal.base.entity.oauth2.ClientEntity;
import com.mzlalal.base.entity.oauth2.vo.OauthCodeVo;
import com.mzlalal.base.entity.oauth2.vo.OauthVo;
import com.mzlalal.base.entity.oauth2.vo.TokenVo;
import com.mzlalal.base.feign.oauth2.OauthFeignApi;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.oauth2.config.oauth2.enums.GrantResponseEnum;
import com.mzlalal.oauth2.service.ClientService;
import com.mzlalal.oauth2.service.impl.NotifyServiceImpl;
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

import java.util.concurrent.TimeUnit;

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

    private final NotifyServiceImpl notifyService;

    private final ClientService clientService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public OauthController(ClientService clientService, BCryptPasswordEncoder passwordEncoder
            , RedisTemplate<String, Object> redisTemplate, NotifyServiceImpl notifyService) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.notifyService = notifyService;
    }

    @Override
    public Result<String> authorizeCode(@RequestBody OauthCodeVo oauthCodeVo) {
        // 检查客户端
        ClientEntity client = clientService.getOneByClientId(oauthCodeVo.getClientId());
        AssertUtil.notNull(client, GlobalResult.OAUTH_FAIL);
        // 获取授权类型
        String[] grantTypeArray = StrUtil.splitToArray(client.getResponseType(), ",");
        // 不存在这输出异常
        AssertUtil.isTrue(ArrayUtil.contains(grantTypeArray, oauthCodeVo.getGrantType())
                , GlobalResult.OAUTH_RESPONSE_TYPE_NOT_CORRECT);
        // 随机数
        int randomInt = RandomUtil.randomInt(10001, 99999);
        // 过期时间15分钟
        redisTemplate.opsForValue()
                .set(GlobalConstant.mailCodeRedisKey(oauthCodeVo.getUsername()), randomInt, 15, TimeUnit.MINUTES);
        // 构建参数
        AccountAuthorizeCodeEntity authorizeCodeEntity = AccountAuthorizeCodeEntity.builder()
                .account(oauthCodeVo.getUsername())
                .authorizeCode(String.valueOf(randomInt))
                .build();
        // 发送请求
        return Result.ok(notifyService.send(authorizeCodeEntity));
    }

    @Override
    public Result<BaseEntity> authorize(@RequestBody OauthVo oauthVo) {
        // 检查客户端
        ClientEntity client = clientService.getOneByClientId(oauthVo.getClientId());
        AssertUtil.notNull(client, GlobalResult.OAUTH_FAIL);
        // 获取授权类型
        String[] responseTypeArray = StrUtil.splitToArray(client.getResponseType(), ",");
        // 不存在这输出异常
        AssertUtil.arrayNotContains(responseTypeArray, oauthVo.getResponseType(), GlobalResult.OAUTH_RESPONSE_TYPE_NOT_CORRECT);
        // 重定向地址为空,则设置默认的重新地址
        if (StrUtil.isBlank(oauthVo.getRedirectUri())) {
            oauthVo.setRedirectUri(client.getRedirectUri());
        }
        // 获取授权码
        return GrantResponseEnum.getEnum(oauthVo.getResponseType()).processGrant(oauthVo);
    }

    @Override
    public Result<BaseEntity> token(@RequestBody TokenVo tokenVo) {
        // 查询客户端
        ClientEntity client = clientService.getOneByClientId(tokenVo.getClientId());
        // 验证
        AssertUtil.notNull(client, GlobalResult.OAUTH_FAIL);
        // 私匙
        AssertUtil.isTrue(passwordEncoder.matches(tokenVo.getClientSecret(), client.getClientSecret()), GlobalResult.OAUTH_FAIL);
        // 授权类别
        AssertUtil.isTrue(StrUtil.contains(client.getResponseType(), tokenVo.getResponseType()), GlobalResult.OAUTH_RESPONSE_TYPE_NOT_CORRECT);
        // 赋值
        OauthVo oauthVo = BeanUtil.copyProperties(tokenVo, OauthVo.class);
        oauthVo.setGrantValue(tokenVo.getCode());
        // 获取授权码
        return GrantResponseEnum.getEnum(tokenVo.getResponseType()).processToken(oauthVo, client);
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
