package com.mzlalal.base.oauth2;

import com.mzlalal.base.entity.oauth2.dto.UserEntity;

/**
 * 用户上下文
 *
 * @author Mzlalal
 * @date 2021/7/26 10:49
 */
public class Oauth2Context {
    /**
     * 用户上下文
     */
    private static final ThreadLocal<UserEntity> CONTEXT = new ThreadLocal<>();

    /**
     * 设置用户信息
     *
     * @param userEntity 用户信息
     */
    public static void setContext(UserEntity userEntity) {
        CONTEXT.set(userEntity);
    }

    /**
     * 获取用户信息
     *
     * @return UserEntity 用户信息
     */
    public static UserEntity get() {
        return CONTEXT.get();
    }

    /**
     * 删除用户信息
     */
    public static void remove() {
        CONTEXT.remove();
    }
}
