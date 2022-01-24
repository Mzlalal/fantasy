package com.mzlalal.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.extra.template.TemplateEngine;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.notify.VerifyCodeEntity;
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
public class NotifyService {
    /**
     * 邮件发送服务
     */
    private final MailNotifyService mailNotifyService;
    /**
     * 模板服务
     */
    private final TemplateEngine templateEngine;

    @Autowired
    public NotifyService(MailNotifyService mailNotifyService, TemplateEngine templateEngine) {
        this.mailNotifyService = mailNotifyService;
        this.templateEngine = templateEngine;
    }

    /**
     * 发送邮件
     *
     * @param verifyCodeEntity 验证码信息
     * @return 邮件ID
     */
    public String send(VerifyCodeEntity verifyCodeEntity) {
        // 验证邮箱格式
        AssertUtil.isTrue(Validator.isEmail(verifyCodeEntity.getUsername())
                , GlobalResult.EMAIL_NOT_CORRECT);
        // 渲染文本
        String content = templateEngine.getTemplate("/notify/mail/accountMailVerifyCode.html")
                .render(BeanUtil.beanToMap(verifyCodeEntity));
        // 发送邮件
        return mailNotifyService.send(verifyCodeEntity.getUsername()
                , "Fantasy-登录验证码", content, true);
    }
}
