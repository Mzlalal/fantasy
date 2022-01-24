package com.mzlalal.base.feign.oauth2;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.vo.CheckVerifyCodeVo;
import com.mzlalal.base.entity.oauth2.vo.OauthVo;
import com.mzlalal.base.entity.oauth2.vo.VerifyCodeVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 登录授权feign
 * 1. 获取验证码(/api/v1/oauth/verify.code),如果使用文本文本明码登录,需要在输入密码错误后验证码图形验证码
 * 2. 根据验证码+用户名+密码(密码可以是邮箱验,短信验证码等)判断登录是否成功(/api/v1/oauth/authorize)
 * 3. 登录成功返回一个GET地址,前端跳转此地址或者ajax访问此地址
 * 4. 第三方服务通过回调接口发送私匙+授权码来生成TOKEN(/api/v1/oauth/create.token)
 *
 * @author Mzlalal88
 * @date 2021/7/28 14:28
 */
@FeignClient(name = GlobalConstant.FANTASY_OAUTH2 + "/api/v1/oauth", url = "${fantasy-oauth2.feign.url}")
public interface OauthFeignApi {

    /**
     * 登录验证码
     *
     * @param verifyCodeVo 登录验证码参数
     * @return Result<String>
     */
    @ApiOperation("登录验证码")
    @PostMapping("/verify.code")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 10)
    Result<String> verifyCode(@Validated @RequestBody VerifyCodeVo verifyCodeVo);

    /**
     * 文本密码登录校验验证码
     *
     * @param checkVerifyCodeVo 文本密码登录校验验证码参数
     * @return Result<String>
     */
    @ApiOperation(value = "校验文本登录验证码", notes = "此接口是可选的,若想实现在第一次文本密码登录失败后显示验证码" +
            ",应在对应情境下请求/api/v1/oauth/verify.code接口,根据responseType=password来生成base64图片校验码" +
            ",此接口验证码比对正确则返回成功" +
            ",若用户验证码没生成过或生成了过期(验证码值在缓存中15分钟过期)或者比对失败则直接返回新的base64图片至data中")
    @PostMapping("/check.verify.code")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 11)
    Result<String> checkVerifyCode(@Validated @RequestBody CheckVerifyCodeVo checkVerifyCodeVo);

    /**
     * 根据用户名及密码校验登录
     * 密码可以是邮箱验证码,图片验证码,短信验证码等
     *
     * @param oauthVo 请求授权参数VO
     * @return Result<BaseEntity>
     */
    @ApiOperation("登录")
    @PostMapping("/authorize")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 20)
    Result<BaseEntity> authorize(@Validated @RequestBody OauthVo oauthVo);

    /**
     * 根据授权码(UUID)获取令牌
     *
     * @param oauthVo 置换TOKEN参数
     * @return Result<BaseEntity>
     */
    @ApiOperation("获取令牌")
    @PostMapping("/create.token")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 30)
    Result<BaseEntity> createToken(@Validated @RequestBody OauthVo oauthVo);

    /**
     * 验证令牌是否有效
     *
     * @param token 令牌
     * @return Result<BaseEntity>
     */
    @ApiOperation("验证令牌")
    @PostMapping("/check.token")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 40)
    Result<BaseEntity> checkToken(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token);

    /**
     * 登出
     * 删除令牌
     *
     * @param token createToken
     * @return Result
     */
    @ApiOperation("注销登出")
    @GetMapping("/logout")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 50)
    Result<Void> logout(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token);
}
