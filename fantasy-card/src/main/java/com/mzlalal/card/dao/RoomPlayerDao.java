package com.mzlalal.card.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 房间内的选手信息dao
 *
 * @author Mzlalal
 * @date 2022-02-10 17:11:39
 */
@Mapper
public interface RoomPlayerDao extends BaseMapper<RoomPlayerEntity> {

    /**
     * 增加分数
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param change 转账数额
     * @return int
     */
    int addPlayerScore(@Param("roomId") String roomId
            , @Param("userId") String userId
            , @Param("change") Integer change);

    /**
     * 减少分数
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param change 转账数额
     * @return int
     */
    int subPlayerScore(@Param("roomId") String roomId
            , @Param("userId") String userId
            , @Param("change") Integer change);
}
