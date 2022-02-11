package com.mzlalal.card.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 房间内的选手信息dao
 *
 * @author Mzlalal
 * @date 2022-02-10 17:11:39
 */
@Mapper
public interface RoomPlayerDao extends BaseMapper<RoomPlayerEntity> {

}
