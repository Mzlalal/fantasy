package com.mzlalal.oauth2.config.oauth2.enums;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.notify.VerifyCodeEntity;
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
        public Result<String> generateVerifyCode(String username) {
            // 随机数
            int randomInt = RandomUtil.randomInt(10001, 99999);
            // 过期时间15分钟
            redisTemplate.opsForValue()
                    .set(GlobalConstant.mailCodeRedisKey(username), String.valueOf(randomInt), 15, TimeUnit.MINUTES);
            // 构建参数
            VerifyCodeEntity verifyCodeEntity = VerifyCodeEntity.builder()
                    .username(username)
                    .code(String.valueOf(randomInt))
                    .build();
            // 发送邮箱验证码
            return Result.ok(notifyService.send(verifyCodeEntity));
        }
    };

    /**
     * 发送验证码给用户名
     * 密码可以是邮箱验证码,图片验证码,短信验证码等
     *
     * @param username 用户名
     * @return Result<String>
     */
    public abstract Result<String> generateVerifyCode(String username);

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
        throw GlobalResult.OAUTH_VALIDATE_TYPE_NOT_CORRECT.boom();
    }
}
