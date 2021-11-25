package com.mzlalal.base.feign.notify;

import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.notify.AccountAuthorizeCodeEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 消息通知feign API
 * 邮件接口
 *
 * @author Mzlalal
 * @date 2021/9/24 10:04
 */
@FeignClient(name = GlobalConstant.FANTASY_OAUTH2 + "/api/v1/notify/mail")
public interface MailNotifyFeignApi {

    /**
     * 发送登录验证码邮件给用户
     *
     * @param authorizeCodeEntity 发送登录验证码邮件参数
     * @return string 消息ID
     */
    @ApiOperation("发送登录验证码邮件")
    @PostMapping("/notify.account.authorize.code")
    Result<String> notifyAccountAuthorizeCode(@RequestBody AccountAuthorizeCodeEntity authorizeCodeEntity);
}
