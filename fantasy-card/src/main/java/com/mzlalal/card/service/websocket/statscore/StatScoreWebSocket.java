package com.mzlalal.card.service.websocket.statscore;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.card.service.RoomPlayerService;
import com.mzlalal.card.service.websocket.session.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * 统计分数
 *
 * @author Mzlalal
 * @date 2022/2/10 10:23
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/stat.score/{roomId}/{token}")
public class StatScoreWebSocket {
    /**
     * 用户会话操作
     */
    private final UserSessionService userSessionService = SpringUtil.getBean(UserSessionService.class);
    /**
     * 房间内的选手操作
     */
    private final RoomPlayerService roomPlayerService = SpringUtil.getBean(RoomPlayerService.class);

    /**
     * 连接成功
     *
     * @param session 会话
     * @param roomId  房间ID
     * @param token   用户TOKEN
     * @throws IOException 会话关闭异常
     */
    @OnOpen
    public void onOpen(Session session
            , @PathParam(value = "roomId") String roomId
            , @PathParam(value = "token") String token) throws IOException {
        // 根据TOKEN获取用户
        UserEntity user = userSessionService.getUserByToken(token);
        // 用户未登录
        if (user == null) {
            session.getBasicRemote().sendText(JSON.toJSONString(GlobalResult.USER_NOT_LOGIN.result()));
            session.close();
            return;
        }
        // 用户ID
        String userId = user.getId();
        // 根据房间ID和用户ID保存会话
        userSessionService.saveByRoomIdAndUserId(roomId, userId, session);
        // 保存用户至房间
        roomPlayerService.playerInit(roomId, user);
    }

    /**
     * 连接关闭
     *
     * @param session 会话
     * @param roomId  房间ID
     * @param token   用户TOKEN
     * @throws IOException 会话关闭异常
     */
    @OnClose
    public void onClose(Session session
            , @PathParam(value = "roomId") String roomId
            , @PathParam(value = "token") String token) throws IOException {
        // 根据TOKEN获取用户
        UserEntity user = userSessionService.getUserByToken(token);
        // 用户未登录
        if (user == null) {
            session.close();
            return;
        }
        // 用户ID
        String userId = user.getId();
        // 根据房间ID和用户ID关闭会话
        userSessionService.closeByRoomIdAndUserId(roomId, userId);
    }

    /**
     * 接收到消息
     *
     * @param str     前端传递的字符信息
     * @param session 会话
     * @param roomId  房间ID
     * @param token   用户TOKEN
     */
    @OnMessage
    public void onMessage(String str, Session session
            , @PathParam(value = "roomId") String roomId
            , @PathParam(value = "token") String token) throws IOException {
        // 根据TOKEN获取用户
        UserEntity user = userSessionService.getUserByToken(token);
        // 用户未登录
        if (user == null) {
            session.getBasicRemote().sendText(JSON.toJSONString(GlobalResult.USER_NOT_LOGIN.result()));
            session.close();
            return;
        }
        // 用户ID
        String userId = user.getId();
        // 广播信息
        roomPlayerService.broadcast(roomId, userId, "", str);
    }
}
