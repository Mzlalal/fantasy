package com.mzlalal.card.service;

import com.mzlalal.base.entity.card.dto.RoomEntity;
import com.mzlalal.base.service.BaseService;

/**
 * 房间service
 *
 * @author Mzlalal
 * @date 2022-02-11 09:18:06
 */
public interface RoomService extends BaseService<RoomEntity> {

    /**
     * 关闭房间
     * 1. 删除房间
     * 2. 删除房间内选手
     * 3. 删除房间消息
     *
     * @param roomId 房间ID
     */
    void closeRoom(String roomId);
}
