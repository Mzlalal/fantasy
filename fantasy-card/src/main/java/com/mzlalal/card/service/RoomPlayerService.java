package com.mzlalal.card.service;

import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.card.req.PlayerHistoryMessageReq;
import com.mzlalal.base.entity.card.req.PlayerOutOrJoinRoomReq;
import com.mzlalal.base.entity.card.req.TransferScoreReq;
import com.mzlalal.base.entity.global.WsResult;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.service.BaseService;

import java.util.List;

/**
 * 房间内选手service
 *
 * @author Mzlalal
 * @date 2022-02-10 17:11:39
 */
public interface RoomPlayerService extends BaseService<RoomPlayerEntity> {

    /**
     * 根据房间ID和用户ID获取选手
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     * @return RoomPlayerEntity
     */
    RoomPlayerEntity getOneByRoomIdAndUserId(String roomId, String userId);

    /**
     * 初始化房间内的选手
     *
     * @param roomId 房间ID
     * @param user   用户信息
     */
    void playerInit(String roomId, UserEntity user);

    /**
     * 用户加入房间
     *
     * @param req 请求
     */
    void playerJoinRoom(PlayerOutOrJoinRoomReq req);

    /**
     * 用户退出房间
     *
     * @param req 请求
     */
    void playerOutRoom(PlayerOutOrJoinRoomReq req);

    /**
     * 根据房间号统计房间内的选手信息
     *
     * @param roomId 房间ID
     * @return StatScoreVo
     */
    List<RoomPlayerEntity> getRoomPlayerListByRoomId(String roomId);

    /**
     * 更新用户状态
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param status 状态
     */
    void updateStatus(String roomId, String userId, String status);

    /**
     * 转账分数
     *
     * @param transferScoreReq 转账分数请求
     */
    void transferScore(TransferScoreReq transferScoreReq);

    /**
     * 根据房间ID,选手ID查询历史消息
     *
     * @param req 查询房间选手的历史消息请求
     * @return List<WsResult>
     */
    List<WsResult<Void>> getRoomHistoryMessage(PlayerHistoryMessageReq req);

    /**
     * 关闭房间
     *
     * @param ids 房间ID
     */
    void closeRoom(String[] ids);
}
