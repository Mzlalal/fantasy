package com.mzlalal.base.common;

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
     * code
     */
    String VALIDATE_CODE = "code";
    /**
     * password
     */
    String VALIDATE_PASSWORD = "password";

    /**
     * 邮箱redis key
     *
     * @param mail 电子邮箱地址
     * @return mail:code:{mail}
     */
    static String mailCodeRedisKey(String mail) {
        return "mail:code:" + mail;
    }

    /**
     * 用户 token redis key
     *
     * @param token 电子邮箱地址
     * @return user:token:{token}
     */
    static String tokenRedisKey(String token) {
        return "user:token:" + token;
    }

    /**
     * 用户名登录错误次数
     *
     * @param username 用户名
     * @return login:error:{username}
     */
    static String loginErrorRedisKey(String username) {
        return "login:error:" + username;
    }
}
