package com.mzlalal.oauth2.config.oauth2.enums;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oauth2.UserEntity;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.oauth2.config.oauth2.service.RedisAuthCodeService;
import com.mzlalal.oauth2.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 登录验证方式
 * 1.邮箱验证码
 * 2.密码
 *
 * @author Mzlalal88
 * @date 2021/7/26 11:22
 */
public enum GrantProvideEnum {
    /**
     * 邮箱验证码登录
     */
    MAIL() {
        @Override
        public String validate(String username, String value) {
            // 邮箱格式
            AssertUtil.isTrue(Validator.isEmail(username), GlobalResult.EMAIL_NOT_CORRECT);
            // 比较redis中的验证码
            StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
            // 获取验证码
            String redisCode = redisTemplate.opsForValue().get(GlobalConstant.mailCodeRedisKey(username));
            // 验证码是否正确
            AssertUtil.isTrue(StrUtil.equals(redisCode, value), GlobalResult.VALIDATE_CODE_NOT_RIGHT);
            // 查询用户信息
            UserEntity userEntity = SpringUtil.getBean(UserService.class).findOneByMail(username)
                    .orElseThrow(GlobalResult.EMAIL_NOT_FOUNT::boom);
            // 存储用户信息并返回授权码
            return SpringUtil.getBean(RedisAuthCodeService.class).store(userEntity);
        }
    },
    /**
     * 密码登录
     */
    PASSWORD() {
        @Override
        public String validate(String username, String value) {
            // 手机格式
            AssertUtil.isTrue(Validator.isMobile(username), GlobalResult.MOBILE_NOT_CORRECT);
            // 密码
            BCryptPasswordEncoder passwordEncoder = SpringUtil.getBean(BCryptPasswordEncoder.class);
            // 查询用户信息
            UserEntity userEntity = SpringUtil.getBean(UserService.class).findOneByMobile(username)
                    .orElseThrow(GlobalResult.MOBILE_NOT_FOUNT::boom);
            // 存储用户信息和授权码
            if (passwordEncoder.matches(value, userEntity.getPassword())) {
                return SpringUtil.getBean(RedisAuthCodeService.class).store(userEntity);
            }
            // 密码不正确
            throw GlobalResult.PASSWORD_NOT_RIGHT.boom();
        }
    };

    /**
     * 验证码
     *
     * @param username 用户名
     * @param value    验证码
     * @return boolean
     */
    public abstract String validate(String username, String value);

    /**
     * 根据授权方式type获取
     *
     * @param type 授权方式
     * @return GrantEnum
     */
    public static GrantProvideEnum getEnum(String type) {
        for (GrantProvideEnum value : GrantProvideEnum.values()) {
            // 枚举名字和type是否相等
            if (StrUtil.equalsIgnoreCase(type, value.name())) {
                return value;
            }
        }
        throw GlobalResult.OAUTH_VALIDATE_TYPE_NOT_CORRECT.boom();
    }
}
