package com.mzlalal.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.extra.template.TemplateEngine;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.notify.AccountAuthorizeCodeEntity;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.notify.service.MailNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息服务
 *
 * @author Mzlalal
 * @date 2021/12/1 21:22
 */
@Service("notifyServiceImpl")
public class NotifyServiceImpl {
    /**
     * 邮件发送服务
     */
    private final MailNotifyService mailNotifyService;
    /**
     * 模板服务
     */
    private final TemplateEngine templateEngine;

    @Autowired
    public NotifyServiceImpl(MailNotifyService mailNotifyService, TemplateEngine templateEngine) {
        this.mailNotifyService = mailNotifyService;
        this.templateEngine = templateEngine;
    }

    public String send(AccountAuthorizeCodeEntity authorizeCodeEntity) {
        // 验证邮箱格式
        AssertUtil.isTrue(Validator.isEmail(authorizeCodeEntity.getAccount())
                , GlobalResult.EMAIL_NOT_CORRECT);
        // 渲染文本
        String content = templateEngine.getTemplate("/notify/mail/notifyAccountAuthorizeCode.html")
                .render(BeanUtil.beanToMap(authorizeCodeEntity));
        // 发送邮件
        return mailNotifyService.send(authorizeCodeEntity.getAccount()
                , "Fantasy-验证码", content, true);
    }
}
