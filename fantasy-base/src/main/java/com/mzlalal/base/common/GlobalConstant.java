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
     * 棋牌室应用名
     */
    String FANTASY_CARD = "fantasy-card";
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
     * 生产环境
     */
    String PRODUCT = "product";
    /**
     * 启用
     */
    String STATUS_ON = "1";
    /**
     * 禁用
     */
    String STATUS_OFF = "0";
    /**
     * 启用
     */
    String DEFAULT_TENANT_ID = "PUBLIC";
    /**
     * 用户展示名HTML
     */
    String USERNAME_DISPLAY = "<span class=\"username-display\">{}</span>";

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
     * @param clientId 客户端
     * @param mail     电子邮箱地址
     * @return String fantasy:mail:code:{clientId}:{mail}
     */
    static String clientIdMailCodeRedisKey(String clientId, String mail) {
        return StrUtil.format("fantasy:mail:code:{}:{}", clientId, mail);
    }

    /**
     * 手机号登录验证码 redis key
     *
     * @param clientId 客户端ID
     * @param mobile   手机号
     * @return String fantasy:password:code:{clientId}:{mobile}
     */
    static String clientIdPasswordCodeRedisKey(String clientId, String mobile) {
        return StrUtil.format("fantasy:password:code:{}:{}", clientId, mobile);
    }

    /**
     * 房间ID redis key
     *
     * @param roomId 房间ID
     * @return String fantasy:room:{roomId}
     */
    static String roomIdRedisKey(String roomId) {
        return "fantasy:room:" + roomId;
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
