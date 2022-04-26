package com.mzlalal.oss.service;

import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DiaryEntity;
import com.mzlalal.base.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 飞虹service
 *
 * @author Mzlalal
 * @date 2022-04-28 20:08:41
 */
public interface DiaryService extends BaseService<DiaryEntity> {

    /**
     * 查询飞虹根据日期分组
     *
     * @param po 查询条件
     * @return Result<Map < Date, List < DiaryEntity>>>
     */
    Result<Map<String, List<DiaryEntity>>> queryDiaryGroupByDate(Po<DiaryEntity> po);
}