package com.mzlalal.base.oauth2;

import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.util.AssertUtil;

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
     * 获取用户ID
     *
     * @return String userId
     */
    public static String getUserId() {
        AssertUtil.notNull(CONTEXT.get(), GlobalResult.USER_NOT_LOGIN);
        return CONTEXT.get().getId();
    }

    /**
     * 获取用户姓名
     *
     * @return String 用户名
     */
    public static String getUsername() {
        AssertUtil.notNull(CONTEXT.get(), GlobalResult.USER_NOT_LOGIN);
        return CONTEXT.get().getUsername();
    }

    /**
     * 获取用户租户信息
     *
     * @return String 租户
     */
    public static String getTenantId() {
        if (CONTEXT.get() != null) {
            return CONTEXT.get().getTenantId();
        }
        return GlobalConstant.DEFAULT_TENANT_ID;
    }

    /**
     * 删除用户信息
     */
    public static void remove() {
        CONTEXT.remove();
    }
}
