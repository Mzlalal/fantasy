package com.mzlalal.card.service;

import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.card.vo.HistoryMessageVo;
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
    RoomPlayerEntity queryOneByRoomIdAndUserId(String roomId, String userId);

    /**
     * 根据房间号统计房间内的选手信息
     *
     * @param roomId 房间ID
     * @return StatScoreVo
     */
    List<RoomPlayerEntity> queryRoomPlayerListByRoomId(String roomId);

    /**
     * 增加分数
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param change 转账数额
     * @return int
     */
    int addRoomPlayerScore(String roomId, String userId, Integer change);

    /**
     * 减少分数
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param change 转账数额
     * @return int
     */
    int subRoomPlayerScore(String roomId, String userId, Integer change);

    /**
     * 更新用户状态
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param status 状态
     */
    void updateRoomPlayerStatus(String roomId, String userId, String status);

    /**
     * 根据房间ID,选手ID查询历史消息
     *
     * @param roomId 房间ID
     * @return List<HistoryMessageVo>
     */
    List<HistoryMessageVo> queryPlayerHistoryMessage(String roomId);

    /**
     * 关闭房间
     *
     * @param ids 房间ID
     */
    void closeRoom(String[] ids);
}
