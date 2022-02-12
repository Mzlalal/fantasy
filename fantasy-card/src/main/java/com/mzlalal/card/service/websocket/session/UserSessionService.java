package com.mzlalal.card.service.websocket.session;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.global.WsResult;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.card.service.RoomPlayerService;
import com.mzlalal.card.service.websocket.session.manager.UserSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    /**
     * 房间内的选手操作
     */
    private RoomPlayerService roomPlayerService;

    public UserSessionService(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Autowired
    public void setRoomPlayerService(RoomPlayerService roomPlayerService) {
        this.roomPlayerService = roomPlayerService;
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
        // 保存用户ID至房间中
        String roomIdRedisKey = GlobalConstant.roomIdRedisKey(roomId);
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
        // 保存用户ID至房间中
        String roomIdRedisKey = GlobalConstant.roomIdRedisKey(roomId);
        stringRedisTemplate.opsForSet().remove(roomIdRedisKey, userId);
    }

    /**
     * 广播信息
     *
     * @param roomId  房间ID
     * @param from    发送者
     * @param message 文本消息
     */
    public void broadcast(String roomId, String from, String message) {
        // 查询当前房间内的选手信息
        List<RoomPlayerEntity> roomPlayerList = roomPlayerService.queryRoomPlayerByRoomId(roomId);
        if (CollUtil.isEmpty(roomPlayerList)) {
            return;
        }
        // msg
        WsResult<RoomPlayerEntity> result = WsResult.okMsg(message);
        // 发送者
        result.setFrom(from);
        // 房间内的选手信息
        result.getPageInfo().setList(roomPlayerList);
        // 广播
        this.broadcast(roomId, result);
    }

    /**
     * 广播信息
     *
     * @param roomId 房间ID
     * @param result 选手分数统计数据
     */
    public void broadcast(String roomId, WsResult<?> result) {
        // result 不能为空
        if (result == null) {
            return;
        }
        // 获取当前房间的人员
        String roomIdRedisKey = GlobalConstant.roomIdRedisKey(roomId);
        Set<String> memberSet = stringRedisTemplate.opsForSet().members(roomIdRedisKey);
        // 房间没有选手了
        if (CollUtil.isEmpty(memberSet)) {
            return;
        }
        // dio当前房间的所有人遍历发送
        UserSessionManager.getByUserIdSet(memberSet).parallelStream().forEach(session -> {
            // 如果为空或者非正常状态,跳出
            if (session == null || !session.isOpen()) {
                return;
            }
            // 获取参数
            Map<String, String> pathParameterMap = session.getPathParameters();
            // 发送给其他人
            try {
                session.getBasicRemote().sendText(JSON.toJSONString(result));
            } catch (IOException e) {
                log.error(JSON.toJSONString(pathParameterMap) + "关闭意外出错", e);
            }
        });
    }
}
