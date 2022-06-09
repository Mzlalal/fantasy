package com.mzlalal.oss.service;

import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DiarySubscribeEntity;
import com.mzlalal.base.service.BaseService;
import com.mzlalal.base.util.Page;

/**
 * 尺墨飞虹订阅表service
 *
 * @author Mzlalal
 * @date 2022-05-29 21:29:53
 */
public interface DiarySubscribeService extends BaseService<DiarySubscribeEntity> {
    /**
     * 查看我的粉丝列表
     *
     * @param po 分页信息
     * @return Page<DiarySubscribeEntity>
     */
    Page<DiarySubscribeEntity> followerList(Po<DiarySubscribeEntity> po);

    /**
     * 查看我的订阅列表
     *
     * @param po 分页信息
     * @return Page<DiarySubscribeEntity>
     */
    Page<DiarySubscribeEntity> subscribeList(Po<DiarySubscribeEntity> po);

    /**
     * 根据关键字搜索用户订阅列表
     *
     * @param po 分页信息
     * @return Page<DiarySubscribeEntity>
     */
    Page<DiarySubscribeEntity> applySubscribeList(Po<String> po);

    /**
     * 通知我的粉丝动态更新
     */
    void notifyFollower();
}