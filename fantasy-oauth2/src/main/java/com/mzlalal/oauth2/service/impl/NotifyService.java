package com.mzlalal.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.template.TemplateEngine;
import com.mzlalal.base.entity.notify.req.SendMailCodeReq;
import com.mzlalal.notify.service.MailNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
     * 发送验证码邮件给用户
     *
     * @param req 邮件验证码参数
     * @return 邮件ID
     */
    public String send(@Validated SendMailCodeReq req) {
        // 渲染文本
        String content = templateEngine.getTemplate("/notify/mail/accountMailVerifyCode.html")
                .render(BeanUtil.beanToMap(req));
        // 发送邮件
        return mailNotifyService.send(req.getMail(), "Fantasy-登录验证码", content, true);
    }
}
