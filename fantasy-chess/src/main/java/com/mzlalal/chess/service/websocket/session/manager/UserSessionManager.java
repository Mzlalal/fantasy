package com.mzlalal.chess.service.websocket.session.manager;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket会话管理
 *
 * @author Mzlalal
 * @date 2022/2/10 11:33
 */
public class UserSessionManager {
    /**
     * 会话管理
     */
    private static final ConcurrentHashMap<String, Session> SESSION_MANAGER = new ConcurrentHashMap<>();

    /**
     * 用户会话放入缓存
     *
     * @param userId  用户ID
     * @param session 会话
     */
    public static void put(String userId, Session session) {
        SESSION_MANAGER.put(userId, session);
    }

    /**
     * 删除用户会话
     *
     * @param userId 用户ID
     */
    public static void remove(String userId) {
        SESSION_MANAGER.remove(userId);
    }

    /**
     * 获取用户会话
     *
     * @param userId 用户ID
     * @return Session 会话
     */
    public static Session getByUserId(String userId) {
        return SESSION_MANAGER.get(userId);
    }

    /**
     * 根据用户ID集合获取会话
     *
     * @param userIdSet 用户ID集合
     * @return Collection<Session> 会话集合
     */
    public static List<Session> getByUserIdSet(Set<String> userIdSet) {
        return getByUserIds(ArrayUtil.toArray(userIdSet, String.class));
    }

    /**
     * 根据用户ID集合获取会话
     *
     * @param userIds 用户ID集合
     * @return Collection<Session> 会话集合
     */
    public static List<Session> getByUserIds(String... userIds) {
        // 返回结果
        ArrayList<Session> arrayList = Lists.newArrayListWithCapacity(userIds.length);
        // 遍历
        for (String userId : userIds) {
            // 查询
            Session session = SESSION_MANAGER.get(userId);
            // 不为空则添加到返回结果
            if (session != null) {
                arrayList.add(session);
            }
        }
        return arrayList;
    }
}
