package com.mzlalal.base.common;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 全局常量类
 *
 * @author Mzlalal
 * @date 2021/5/24 21:39
 **/
public interface GlobalConstant {
    /**
     * 邮箱
     */
    String F_AUTHORIZATION = "F-Authorization";
    /**
     * 字典应用名
     */
    String FANTASY_OSS = "fantasy-oss";
    /**
     * 字典应用名
     */
    String FANTASY_DICT = "fantasy-dict";
    /**
     * 授权服务器应用名
     */
    String FANTASY_OAUTH2 = "fantasy-oauth2";
    /**
     * 纪念日应用名
     */
    String FANTASY_DAY_MATTER = "fantasy-day-matter";
    /**
     * 邮箱
     */
    String MAIL = "mail";
    /**
     * 密码
     */
    String PASSWORD = "password";

    /**
     * UUID与clientId绑定的授权码 redis key
     *
     * @param clientId 机构ID
     * @return String fantasy:{clientId}:{uuid}
     */
    static String clientIdAuthCodeRedisKey(String clientId) {
        return StrUtil.format("fantasy:{}:{}", clientId, IdUtil.fastSimpleUUID());
    }

    /**
     * 邮箱 redis key
     *
     * @param mail 电子邮箱地址
     * @return String fantasy:mail:code:{mail}
     */
    static String mailCodeRedisKey(String mail) {
        return "fantasy:mail:code:" + mail;
    }

    /**
     * 手机号登录验证码 redis key
     *
     * @param mobile 手机号
     * @return String fantasy:mail:code:{mail}
     */
    static String passwordCodeRedisKey(String mobile) {
        return "fantasy:password:code:" + mobile;
    }

    /**
     * 用户 token redis key
     *
     * @param token 电子邮箱地址
     * @return String fantasy:user:token:{token}
     */
    static String tokenRedisKey(String token) {
        return "fantasy:user:token:" + token;
    }

    /**
     * 用户名登录错误次数
     *
     * @param username 用户名
     * @return String fantasy:login:error:{username}
     */
    static String loginErrorRedisKey(String username) {
        return "fantasy:login:error:" + username;
    }
}
