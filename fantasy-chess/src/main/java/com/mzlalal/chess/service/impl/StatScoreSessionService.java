package com.mzlalal.chess.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.chess.dto.RoomEntity;
import com.mzlalal.base.entity.chess.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.chess.req.PlayerOutOrJoinRoomReq;
import com.mzlalal.base.entity.chess.req.TransferScoreReq;
import com.mzlalal.base.entity.chess.vo.HistoryMessageVo;
import com.mzlalal.base.entity.global.WsResult;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.chess.service.RoomPlayerService;
import com.mzlalal.chess.service.RoomService;
import com.mzlalal.chess.service.websocket.session.manager.UserSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 会话广播服务
 *
 * @author Mzlalal
 * @date 2022/2/28 11:53
 */
@Slf4j
@Component
public class StatScoreSessionService {
    /**
     * 房间操作
     */
    private final RoomService roomService;
    /**
     * 房间内的选手操作
     */
    private final RoomPlayerService roomPlayerService;
    /**
     * string=>对象 redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * string redis操作模板
     */
    private final StringRedisTemplate stringRedisTemplate;
    /**
     * redisson数据源
     */
    private final RedissonClient redissonClient;

    public StatScoreSessionService(RoomService roomService, RoomPlayerService roomPlayerService
            , RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate
            , RedissonClient redissonClient) {
        this.roomService = roomService;
        this.roomPlayerService = roomPlayerService;
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
    }

    /**
     * 初始化房间内的选手
     *
     * @param roomId 房间ID
     * @param user   用户信息
     */
    public void roomPlayerInit(String roomId, UserEntity user) {
        // 校验用户
        AssertUtil.notNull(user, "用户不存在");
        String userId = user.getId();
        String username = user.getUsername();
        // 检验房间
        RoomEntity room = roomService.getById(roomId);
        AssertUtil.notNull(room, "房间不存在");

        // 是否已上桌
        RoomPlayerEntity existPlayer = roomPlayerService.queryOneByRoomIdAndUserId(roomId, userId);
        if (existPlayer != null) {
            // 发送房间内的选手状态
            this.broadcastPlayerStatus(roomId);
            return;
        }

        // 未上桌,初始化选手信息
        RoomPlayerEntity roomPlayer = RoomPlayerEntity.builder()
                .id(userId)
                .playerName(username)
                .playerScore(0)
                .playerStatus(GlobalConstant.STATUS_ON)
                .roomId(roomId)
                .roomName(room.getName())
                .build();
        AssertUtil.isTrue(roomPlayerService.save(roomPlayer), "上桌失败,请稍后再试");

        // 上桌消息
        PlayerOutOrJoinRoomReq req = PlayerOutOrJoinRoomReq.builder()
                .roomId(roomId)
                .userId(userId)
                .username(username)
                .build();
        this.playerJoinRoom(req);
    }

    /**
     * 选手上桌
     *
     * @param req 请求
     */
    public void playerJoinRoom(@Validated PlayerOutOrJoinRoomReq req) {
        // 用户ID
        String userId = req.getUserId();
        // 房间ID
        String roomId = req.getRoomId();

        // 用户退出房间
        roomPlayerService.updateRoomPlayerStatus(roomId, userId, GlobalConstant.STATUS_ON);

        // 上桌消息
        String message = StrUtil.format(GlobalConstant.USERNAME_DISPLAY + "上桌了", req.getUsername());
        // 广播消息
        this.broadcast(roomId, userId, message);
    }

    /**
     * 选手下桌
     *
     * @param req 请求
     */
    public void playerOutRoom(@Validated PlayerOutOrJoinRoomReq req) {
        // 用户ID
        String userId = req.getUserId();
        // 房间ID
        String roomId = req.getRoomId();

        // 用户退出房间
        roomPlayerService.updateRoomPlayerStatus(roomId, userId, GlobalConstant.STATUS_OFF);

        // 下桌消息
        String message = StrUtil.format(GlobalConstant.USERNAME_DISPLAY + "下桌了", req.getUsername());
        // 广播消息
        this.broadcast(roomId, userId, message);
    }

    /**
     * 转账分数
     *
     * @param req 请求
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferScore(TransferScoreReq req) {
        String roomId = req.getRoomId();
        String from = req.getFrom();
        String to = req.getTo();
        Integer change = req.getChange();

        String lockRedisKey = GlobalConstant.transferInRoom(roomId);
        RLock lock = redissonClient.getLock(lockRedisKey);
        try {
            // 获取锁
            lock.lock();

            // 检查发起人状态
            RoomPlayerEntity fromPlayer = roomPlayerService.queryOneByRoomIdAndUserId(roomId, from);
            AssertUtil.equals(fromPlayer.getPlayerStatus(), GlobalConstant.STATUS_ON, GlobalResult.SUB_PLAYER_STATUS_OFF);

            // 检查接收人状态
            RoomPlayerEntity toPlayer = roomPlayerService.queryOneByRoomIdAndUserId(roomId, to);
            AssertUtil.equals(toPlayer.getPlayerStatus(), GlobalConstant.STATUS_ON, GlobalResult.ADD_PLAYER_STATUS_OFF);

            // 扣除发起人分数
            int sub = roomPlayerService.subRoomPlayerScore(roomId, from, change);
            AssertUtil.isTrue(sub > 0, GlobalResult.SUB_SCORE_FAIL);

            // 增加接收人分数
            int add = roomPlayerService.addRoomPlayerScore(roomId, to, change);
            AssertUtil.isTrue(add > 0, GlobalResult.ADD_SCORE_FAIL);

            // 发送消息到房间
            this.broadcast(roomId, from, to, req.getMessage());
        } finally {
            // 解锁
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    /**
     * 发送房间内的选手消息
     *
     * @param roomId 房间ID
     */
    public void broadcastPlayerStatus(String roomId) {
        this.broadcast(roomId, "", "");
    }

    /**
     * 广播信息
     *
     * @param roomId  房间ID
     * @param from    发送者
     * @param message 文本消息
     */
    public void broadcast(String roomId, String from, String message) {
        this.broadcast(roomId, from, from, message);
    }

    /**
     * 广播信息
     *
     * @param roomId  房间ID
     * @param from    发送者
     * @param to      接收人
     * @param message 文本消息
     */
    public void broadcast(String roomId, String from, String to, String message) {
        // 查询当前房间内的选手信息
        List<RoomPlayerEntity> roomPlayerList = roomPlayerService.queryRoomPlayerListByRoomId(roomId);
        if (CollUtil.isEmpty(roomPlayerList)) {
            return;
        }
        // msg
        WsResult<RoomPlayerEntity> result = WsResult.okMsg(message);
        // 发送者
        result.setFrom(from);
        // 接收人
        result.setTo(to);
        // 房间内的选手信息
        result.getPage().setList(roomPlayerList);
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
        String roomIdRedisKey = GlobalConstant.userIdsInRoom(roomId);
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
        // 保存消息
        this.saveRoomMessage(roomId, result);
    }

    /**
     * 保存房间消息
     *
     * @param roomId 房间ID
     * @param result 消息
     */
    public void saveRoomMessage(String roomId, WsResult<?> result) {
        // 返回结果赋值给历史消息
        HistoryMessageVo historyMessageVo = new HistoryMessageVo();
        BeanUtil.copyProperties(result, historyMessageVo);

        // 如果from没有则不保存至redis
        if (StrUtil.isBlank(result.getFrom())) {
            return;
        }

        // 保存到redis
        String redisKey = GlobalConstant.messageInRoom(roomId);
        redisTemplate.opsForSet().add(redisKey, historyMessageVo);
    }
}
