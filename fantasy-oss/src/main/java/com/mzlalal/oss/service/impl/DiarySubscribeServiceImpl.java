package com.mzlalal.oss.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DiarySubscribeEntity;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.Page;
import com.mzlalal.oss.dao.DiarySubscribeDao;
import com.mzlalal.oss.service.DiarySubscribeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 尺墨飞虹订阅表ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-05-29 21:29:53
 */
@Service("diarySubscribeServiceImpl")
public class DiarySubscribeServiceImpl extends ServiceImpl<DiarySubscribeDao, DiarySubscribeEntity> implements DiarySubscribeService {

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public Page<DiarySubscribeEntity> queryPage(Po<DiarySubscribeEntity> po) {
        // 创建分页条件
        com.github.pagehelper.Page<DiarySubscribeEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询参数
        LambdaQueryWrapper<DiarySubscribeEntity> wrapper = new LambdaQueryWrapper<>();

        // 查询关注
        String createBy = po.getEntity().getCreateBy();
        if (StrUtil.isNotBlank(createBy)) {
            wrapper.eq(DiarySubscribeEntity::getCreateBy, createBy);
        }
        // 查询粉丝
        String subscribeUserId = po.getEntity().getSubscribeUserId();
        if (StrUtil.isNotBlank(subscribeUserId)) {
            wrapper.eq(DiarySubscribeEntity::getSubscribeUserId, subscribeUserId);
        }

        // 订阅状态
        Integer subscribeStatus = po.getEntity().getSubscribeStatus();
        if (subscribeStatus != null) {
            wrapper.eq(DiarySubscribeEntity::getSubscribeStatus, subscribeStatus);
        }
        // 根据时间降序
        wrapper.orderByDesc(DiarySubscribeEntity::getCreateTime);
        // 查询结果集
        List<DiarySubscribeEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new Page<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    public Page<DiarySubscribeEntity> followerList(Po<DiarySubscribeEntity> po) {
        DiarySubscribeEntity entity = po.getEntity();
        if (entity == null) {
            entity = new DiarySubscribeEntity();
        }
        // 设置创建人
        entity.setSubscribeUserId(Oauth2Context.getUserIdElseThrow());
        return this.queryPage(po);
    }

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public Page<DiarySubscribeEntity> subscribeList(Po<DiarySubscribeEntity> po) {
        DiarySubscribeEntity entity = po.getEntity();
        if (entity == null) {
            entity = new DiarySubscribeEntity();
        }
        // 设置订阅人
        entity.setCreateBy(Oauth2Context.getUserIdElseThrow());
        return this.queryPage(po);
    }

    @Override
    public Page<DiarySubscribeEntity> applySubscribeList(Po<String> po) {
        // 创建分页条件
        com.github.pagehelper.Page<DiarySubscribeEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询列表
        List<DiarySubscribeEntity> entityList = baseMapper.queryApplySubscribeUsernameBySearchKeyword(po.getEntity()
                , Oauth2Context.getUserIdElseThrow());
        // 返回结果
        return new Page<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }
}