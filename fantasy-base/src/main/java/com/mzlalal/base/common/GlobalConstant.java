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
    String FANTASY_CHESS = "fantasy-chess";
    /**
     * 授权服务器应用名
     */
    String FANTASY_OAUTH2 = "fantasy-oauth2";
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
     * 默认用户ID
     */
    String DEFAULT_USER_ID = "1";
    /**
     * 默认用户名
     */
    String DEFAULT_USERNAME = "系统";
    /**
     * 默认超管角色
     */
    String DEFAULT_MANAGE_ROLE = "10000";
    /**
     * 默认普通角色
     */
    String DEFAULT_NORMAL_ROLE = "20000";
    /**
     * 启用
     */
    String DEFAULT_TENANT_ID = "PUBLIC";
    /**
     * 用户展示名HTML
     */
    String USERNAME_DISPLAY = "<span class=\"username-display\">{}</span>";

    /**
     * UUID与clientKey绑定的授权码 redis key
     *
     * @param clientKey 客户端Key
     * @return String "fantasy:client:auth:code:{clientKey}:{uuid}
     */
    static String clientAuthCode(String clientKey) {
        return StrUtil.format("fantasy:client:auth:code:{}:{}", clientKey, IdUtil.fastSimpleUUID());
    }

    /**
     * 邮箱 redis key
     *
     * @param clientKey 客户端Key
     * @param mail      电子邮箱地址
     * @return String fantasy:client:mail:code:{clientKey}:{mail}
     */
    static String clientMailCode(String clientKey, String mail) {
        return StrUtil.format("fantasy:client:mail:code:{}:{}", clientKey, mail);
    }

    /**
     * 手机号登录验证码 redis key
     *
     * @param clientKey 客户端Key
     * @param mobile    手机号
     * @return String fantasy:client:mobile:code:{clientKey}:{mobile}
     */
    static String clientMobileCode(String clientKey, String mobile) {
        return StrUtil.format("fantasy:client:mobile:code:{}:{}", clientKey, mobile);
    }

    /**
     * 房间内的用户ID集合 redis key
     *
     * @param roomId 房间ID
     * @return String fantasy:room:session:{roomId}
     */
    static String userIdsInRoom(String roomId) {
        return "fantasy:room:session:" + roomId;
    }

    /**
     * 房间内的消息 redis key
     *
     * @param roomId 房间ID
     * @return String fantasy:room:message:{roomId}
     */
    static String messageInRoom(String roomId) {
        return "fantasy:room:message:" + roomId;
    }

    /**
     * 房间内的转账 redis key
     *
     * @param roomId 房间ID
     * @return String fantasy:room:transfer:{roomId}
     */
    static String transferInRoom(String roomId) {
        return "fantasy:room:transfer:" + roomId;
    }

    /**
     * 用户token redis key
     *
     * @param token 电子邮箱地址
     * @return String fantasy:user:token:{token}
     */
    static String userToken(String token) {
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

    /**
     * 待办调度redisKey
     *
     * @return String fantasy:notify:schedule
     */
    static String todoNotifySchedule() {
        return StrUtil.format("fantasy:todo:notify:schedule");
    }

    /**
     * 待办重复提醒redisKey
     *
     * @return String fantasy:notify:repeat
     */
    static String todoNotifyLazyMode() {
        return StrUtil.format("fantasy:todo:notify:lazy:mode");
    }
}
