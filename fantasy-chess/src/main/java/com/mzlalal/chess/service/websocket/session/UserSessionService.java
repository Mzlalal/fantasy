package com.mzlalal.chess.service.websocket.session;

import com.alibaba.fastjson.JSON;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.chess.service.websocket.session.manager.UserSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;

/**
 * websocket用户会话操作
 *
 * @author Mzlalal
 * @date 2022/2/10 11:40
 */
@Slf4j
@Component
public class UserSessionService {
    /**
     * string=>对象 redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * string redis操作模板
     */
    private final StringRedisTemplate stringRedisTemplate;

    public UserSessionService(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 根据用户TOKEN获取用户信息
     *
     * @param token 用户TOKEN
     * @return true 用户已缓存 false 用户未登录
     */
    public UserEntity getUserByToken(String token) {
        // 根据TOKEN获取redis用户信息
        return (UserEntity) redisTemplate.opsForValue().get(GlobalConstant.tokenRedisKey(token));
    }

    /**
     * 根据房间ID和用户ID保存会话
     *
     * @param roomId  房间ID
     * @param userId  用户ID
     * @param session 会话
     */
    public void saveByRoomIdAndUserId(String roomId, String userId, Session session) {
        // 保存之间判断是否存在旧连接
        Session oldSession = UserSessionManager.getByUserId(userId);
        if (oldSession != null) {
            // 存在旧连接,关闭
            try {
                oldSession.getBasicRemote().sendText(JSON.toJSONString(GlobalResult.ANOTHER_LOGIN.result()));
                oldSession.close();
            } catch (IOException e) {
                log.error(JSON.toJSONString(oldSession.getPathParameters()) + "关闭意外出错", e);
            }
        }
        // 保存用户会话
        UserSessionManager.put(userId, session);
        // 保存用户会话至redis房间中
        String roomIdRedisKey = GlobalConstant.roomSessionRedisKey(roomId);
        stringRedisTemplate.opsForSet().add(roomIdRedisKey, userId);
    }

    /**
     * 根据房间ID和用户ID关闭会话
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     */
    public void closeByRoomIdAndUserId(String roomId, String userId) {
        // 保存用户会话
        UserSessionManager.remove(userId);
        // 保存用户会话至redis房间中
        String roomIdRedisKey = GlobalConstant.roomSessionRedisKey(roomId);
        stringRedisTemplate.opsForSet().remove(roomIdRedisKey, userId);
    }
}
