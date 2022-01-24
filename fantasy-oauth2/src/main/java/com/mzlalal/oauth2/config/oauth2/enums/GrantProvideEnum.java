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
 * 提供的登录方式,需要先登录成功再置换用户TOKEN
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
        /**
         * 用户服务
         */
        private final UserService userService = SpringUtil.getBean(UserService.class);
        /**
         * redis授权码服务
         */
        private final RedisAuthCodeService redisAuthCodeService = SpringUtil.getBean(RedisAuthCodeService.class);
        /**
         * redis授权码服务
         */
        private final StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

        @Override
        public String processLogin(String username, String password) {
            // 邮箱格式
            AssertUtil.isTrue(Validator.isEmail(username), GlobalResult.EMAIL_NOT_CORRECT);
            // 用户名的邮箱验证码redis key
            String mailCodeRedisKey = GlobalConstant.mailCodeRedisKey(username);
            // 获取验证码
            String redisCode = redisTemplate.opsForValue().get(mailCodeRedisKey);
            // 删除只能使用一次
            redisTemplate.delete(mailCodeRedisKey);
            // 验证码是否正确
            AssertUtil.equals(redisCode, password, GlobalResult.VALIDATE_CODE_NOT_RIGHT);
            // 查询用户信息
            UserEntity userEntity = userService.findOneByMail(username).orElseThrow(GlobalResult.EMAIL_NOT_FOUNT::boom);
            // 存储用户信息并返回授权码
            return redisAuthCodeService.store(userEntity);
        }
    },
    /**
     * 文本密码登录
     */
    PASSWORD() {
        /**
         * 用户服务
         */
        private final UserService userService = SpringUtil.getBean(UserService.class);
        /**
         * 密码工具
         */
        private final BCryptPasswordEncoder passwordEncoder = SpringUtil.getBean(BCryptPasswordEncoder.class);
        /**
         * redis授权码服务
         */
        private final RedisAuthCodeService redisAuthCodeService = SpringUtil.getBean(RedisAuthCodeService.class);

        @Override
        public String processLogin(String username, String password) {
            // 手机格式
            AssertUtil.isTrue(Validator.isMobile(username), GlobalResult.MOBILE_NOT_CORRECT);
            // 查询用户信息
            UserEntity userEntity = userService.findOneByMobile(username)
                    .orElseThrow(GlobalResult.MOBILE_NOT_FOUNT::boom);
            // 存储用户信息和授权码
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return redisAuthCodeService.store(userEntity);
            }
            // 密码不正确
            throw GlobalResult.PASSWORD_NOT_RIGHT.boom();
        }
    };

    /**
     * 验证用户名与密码
     * 密码可以是邮箱验证码,文本密码,短信验证码等
     *
     * @param username 用户名
     * @param password 文本密码/验证码
     * @return String
     */
    public abstract String processLogin(String username, String password);

    /**
     * 根据授权方式type获取
     *
     * @param type 授权方式
     * @return GrantProvideEnum
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
