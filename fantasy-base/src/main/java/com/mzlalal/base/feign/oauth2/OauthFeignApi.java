package com.mzlalal.base.feign.oauth2;

import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.vo.OauthCodeVo;
import com.mzlalal.base.entity.oauth2.vo.OauthVo;
import com.mzlalal.base.entity.oauth2.vo.TokenVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
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
@FeignClient(name = GlobalConstant.FANTASY_OAUTH2 + "/api/v1/oauth")
public interface OauthFeignApi {

    /**
     * 登录验证码
     *
     * @param oauthCodeVo 登录验证码参数
     * @return Result<BaseEntity>
     */
    @ApiOperation("登录验证码")
    @PostMapping("/authorize.code")
    Result<String> authorizeCode(@RequestBody OauthCodeVo oauthCodeVo);

    /**
     * 登录
     *
     * @param oauthVo 授权参数
     * @return Result<BaseEntity>
     */
    @ApiOperation("登录")
    @PostMapping("/authorize")
    Result<BaseEntity> authorize(@RequestBody OauthVo oauthVo);

    /**
     * 获取令牌
     *
     * @param tokenVo 授权参数
     * @return Result<BaseEntity>
     */
    @ApiOperation("获取令牌")
    @PostMapping("/token")
    Result<BaseEntity> token(@RequestBody TokenVo tokenVo);

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @return Result<BaseEntity>
     */
    @ApiOperation("验证令牌")
    @PostMapping("/checkToken")
    Result<BaseEntity> checkToken(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token);

    /**
     * 登出
     *
     * @param token token
     * @return Result
     */
    @ApiOperation("注销登出")
    @GetMapping("/logout")
    Result<Void> logout(@RequestHeader(GlobalConstant.F_AUTHORIZATION) String token);
}
