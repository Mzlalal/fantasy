package com.mzlalal.oauth2.controller.notify;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.extra.template.TemplateEngine;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.notify.AccountAuthorizeCodeEntity;
import com.mzlalal.base.feign.notify.MailNotifyFeignApi;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.notify.service.MailNotifyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件通知controller
 *
 * @author Mzlalal
 * @date 2021-08-23 09:55:38
 */
@Api(tags = "邮件通知")
@RestController
@RequestMapping("/api/v1/notify/mail")
public class MailNotifyController implements MailNotifyFeignApi {
    /**
     * 邮件发送服务
     */
    private final MailNotifyService mailNotifyService;
    /**
     * 模板服务
     */
    private final TemplateEngine templateEngine;

    @Autowired
    public MailNotifyController(MailNotifyService mailNotifyService, TemplateEngine templateEngine) {
        this.mailNotifyService = mailNotifyService;
        this.templateEngine = templateEngine;
    }

    @Override
    public Result<String> notifyAccountAuthorizeCode(@RequestBody AccountAuthorizeCodeEntity authorizeCodeEntity) {
        // 验证邮箱格式
        AssertUtil.isTrue(Validator.isEmail(authorizeCodeEntity.getAccount())
                , GlobalResult.EMAIL_NOT_CORRECT);
        // 渲染文本
        String content = templateEngine
                .getTemplate("/notify/mail/notifyAccountAuthorizeCode.html")
                .render(BeanUtil.beanToMap(authorizeCodeEntity));
        // 发送邮件
        String messageId = mailNotifyService.send(authorizeCodeEntity.getAccount()
                , "Fantasy-验证码", content, true);
        return Result.ok(messageId);
    }
}
