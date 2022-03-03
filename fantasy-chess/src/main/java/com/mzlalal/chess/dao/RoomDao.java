package com.mzlalal.chess.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.chess.dto.RoomEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 房间dao
 *
 * @author Mzlalal
 * @date 2022-02-11 09:18:06
 */
@Mapper
public interface RoomDao extends BaseMapper<RoomEntity> {

}
