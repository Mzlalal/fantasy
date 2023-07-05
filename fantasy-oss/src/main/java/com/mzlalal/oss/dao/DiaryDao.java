package com.mzlalal.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oss.dto.DiaryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 飞虹dao
 *
 * @author Mzlalal
 * @date 2022-04-28 20:08:41
 */
@Mapper
public interface DiaryDao extends BaseMapper<DiaryEntity> {

    /**
     * 查询最近的日期分组最近的日记
     *
     * @param subscribeUserIdList 订阅用户id
     * @param diaryContent        日记内容
     * @return 最近的日期
     */
    List<Date> queryRecentDate(@Param("subscribeUserIdList") List<String> subscribeUserIdList, @Param("diaryContent") String diaryContent);
}
