package com.mzlalal.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oss.dto.DiarySubscribeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 根据用户ID列表查询用户邮箱
     *
     * @param userIdList 用户ID列表
     * @return List<String> 邮箱
     */
    List<String> queryMailByUserIdList(@Param("userIdList") List<String> userIdList);

    /**
     * 根据关键字搜索用户订阅列表
     *
     * @param searchKeyword 关键字
     * @param currentUserId 当前用户
     * @return String
     */
    List<DiarySubscribeEntity> queryApplySubscribeUsernameBySearchKeyword(@Param("searchKeyword") String searchKeyword
            , @Param("currentUserId") String currentUserId);
}
