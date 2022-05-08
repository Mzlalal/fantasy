package com.mzlalal.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oss.dto.DiarySubscribeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 尺墨飞虹订阅表dao
 *
 * @author Mzlalal
 * @date 2022-05-29 21:29:53
 */
@Mapper
public interface DiarySubscribeDao extends BaseMapper<DiarySubscribeEntity> {

    /**
     * 根据用户ID查询用户姓名
     *
     * @param id 用户ID
     * @return String
     */
    String queryUsernameById(@Param("id") String id);
}
