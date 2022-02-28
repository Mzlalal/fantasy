package com.mzlalal.base.feign.oauth2;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * TOKEN令牌操作
 *
 * @author Mzlalal
 * @date 2022/2/8 11:19
 */
@FeignClient(contextId = "TokenFeignApi" , name = GlobalConstant.FANTASY_OAUTH2, url = "${fantasy-oauth2.feign.url}"
        , path = "/api/v1/token")
public interface TokenFeignApi {

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
