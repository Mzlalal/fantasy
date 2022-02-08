package com.mzlalal.oauth2.config.oauth2.enums;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.notify.req.SendMailCodeReq;
import com.mzlalal.oauth2.service.impl.NotifyService;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 验证码方式
 *
 * @author Mzlalal
 * @date 2022/1/24 21:28
 */
public enum VerifyCodeProvideEnum {
    /**
     * 生成邮箱验证码
     */
    MAIL() {
        /**
         * 用户服务
         */
        private final NotifyService notifyService = SpringUtil.getBean(NotifyService.class);
        /**
         * string redis
         */
        private final StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

        @Override
        public Result<String> createVerifyCode(String username, String clientId) {
            // 随机四位数
            int randomInt = RandomUtil.randomInt(1001, 9999);
            // 过期时间15分钟
            redisTemplate.opsForValue()
                    .set(GlobalConstant.clientIdMailCodeRedisKey(clientId, username), String.valueOf(randomInt), 15, TimeUnit.MINUTES);
            // 构建参数
            SendMailCodeReq sendMailCodeReq = SendMailCodeReq.builder()
                    .username(username)
                    .code(String.valueOf(randomInt))
                    .build();
            // 发送邮箱验证码
            return Result.ok(notifyService.send(sendMailCodeReq));
        }
    },
    /**
     * 文本密码登录使用传统的图片验证码
     * 图片验证码使用base64返回
     */
    PASSWORD() {
        /**
         * string redis
         */
        private final StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

        @Override
        public Result<String> createVerifyCode(String username, String clientId) {
            // 定义图形验证码的长、宽、验证码字符数、干扰线宽度
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
            // 图形验证码写出，可以写出到文件，也可以写出到流
            String encode = Base64.encode(captcha.getImageBytes());
            // 验证码最多过期时间15分钟
            redisTemplate.opsForValue()
                    .set(GlobalConstant.clientIdPasswordCodeRedisKey(clientId, username), captcha.getCode(), 15, TimeUnit.MINUTES);
            // 返回base64给前端渲染
            return Result.ok(encode);
        }
    };

    /**
     * 发送验证码给用户名
     * 密码可以是邮箱验证码,图片验证码,短信验证码等
     *
     * @param username 用户名
     * @param clientId 客户端ID
     * @return Result<String>
     */
    public abstract Result<String> createVerifyCode(String username, String clientId);

    /**
     * 根据验证码方式type获取
     *
     * @param type 授权方式
     * @return VerifyCodeProvideEnum
     */
    public static VerifyCodeProvideEnum getEnum(String type) {
        for (VerifyCodeProvideEnum value : VerifyCodeProvideEnum.values()) {
            // 枚举名字和type是否相等
            if (StrUtil.equalsIgnoreCase(type, value.name())) {
                return value;
            }
        }
        throw GlobalResult.OAUTH_RESPONSE_TYPE_NOT_CORRECT.boom();
    }
}
