package com.mzlalal.chess.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.chess.dto.RoomHistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算完成的房间dao
 *
 * @author Mzlalal
 * @date 2022-02-22 11:23:57
 */
@Mapper
public interface RoomHistoryDao extends BaseMapper<RoomHistoryEntity> {

    /**
     * 根据用户ID查询参与的房间结算
     *
     * @param userId 用户ID
     * @return List<RoomHistoryEntity>
     */
    List<RoomHistoryEntity> queryRoomHistoryByUserId(@Param("userId") String userId);

    /**
     * 统计用户ID查询参与的房间结算数量
     *
     * @param userId 用户ID
     * @return Long 数量
     */
    Long countRoomHistoryByUserId(@Param("userId") String userId);
}
