package com.mzlalal.base.feign.oauth2;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.vo.OauthVo;
import com.mzlalal.base.entity.oauth2.vo.VerifyCodeVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 登录授权feign
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
     * @return Result<BaseEntity>
     */
    @ApiOperation("登录验证码")
    @PostMapping("/verify.code")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 1)
    Result<String> verifyCode(@RequestBody VerifyCodeVo verifyCodeVo);

    /**
     * 根据用户名及密码校验登录
     * 密码可以是邮箱验证码,图片验证码,短信验证码等
     *
     * @param oauthVo 请求授权参数VO
     * @return Result<BaseEntity>
     */
    @ApiOperation("登录")
    @PostMapping("/authorize")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 2)
    Result<BaseEntity> authorize(@RequestBody OauthVo oauthVo);

    /**
     * 根据授权码(UUID)获取令牌
     *
     * @param oauthVo 置换TOKEN参数
     * @return Result<BaseEntity>
     */
    @ApiOperation("获取令牌")
    @PostMapping("/create.token")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 3)
    Result<BaseEntity> createToken(@RequestBody OauthVo oauthVo);

    /**
     * 验证令牌是否有效
     *
     * @param token 令牌
     * @return Result<BaseEntity>
     */
    @ApiOperation("验证令牌")
    @PostMapping("/check.token")
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 4)
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
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 5)
    Result<Void> logout(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token);
}
