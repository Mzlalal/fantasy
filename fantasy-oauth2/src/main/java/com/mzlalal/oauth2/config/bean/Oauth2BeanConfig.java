package com.mzlalal.oauth2.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * bean注册
 *
 * @author Mzlalal88
 * @date 2021/7/28 14:04
 */
@Configuration
public class Oauth2BeanConfig {

    /**
     * 密码加密器
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
