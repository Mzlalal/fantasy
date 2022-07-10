package com.mzlalal.oss.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DiarySubscribeEntity;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.notify.service.MailNotifyService;
import com.mzlalal.oss.dao.DiarySubscribeDao;
import com.mzlalal.oss.service.DiarySubscribeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 尺墨飞虹订阅表ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-05-29 21:29:53
 */
@Service("diarySubscribeServiceImpl")
@AllArgsConstructor
public class DiarySubscribeServiceImpl extends ServiceImpl<DiarySubscribeDao, DiarySubscribeEntity> implements DiarySubscribeService {
    /**
     * 邮件提醒service
     */
    private final MailNotifyService mailNotifyService;

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public FantasyPage<DiarySubscribeEntity> queryPage(Po<DiarySubscribeEntity> po) {
        // 创建分页条件
        Page<DiarySubscribeEntity> pageResult = this.createPageQuery(po.getPageInfo());
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
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    public FantasyPage<DiarySubscribeEntity> followerList(Po<DiarySubscribeEntity> po) {
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
    public FantasyPage<DiarySubscribeEntity> subscribeList(Po<DiarySubscribeEntity> po) {
        DiarySubscribeEntity entity = po.getEntity();
        if (entity == null) {
            entity = new DiarySubscribeEntity();
        }
        // 设置订阅人
        entity.setCreateBy(Oauth2Context.getUserIdElseThrow());
        return this.queryPage(po);
    }

    @Override
    public FantasyPage<DiarySubscribeEntity> applySubscribeList(Po<String> po) {
        // 创建分页条件
        Page<DiarySubscribeEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询列表
        List<DiarySubscribeEntity> entityList = baseMapper.queryApplySubscribeUsernameBySearchKeyword(po.getEntity()
                , Oauth2Context.getUserIdElseThrow());
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    public void notifyFollower() {
        // 查询订阅我的粉丝列表
        List<DiarySubscribeEntity> entityList = baseMapper.selectList(Wrappers.<DiarySubscribeEntity>lambdaQuery()
                // 只查询创建人的列
                .select(DiarySubscribeEntity::getCreateBy)
                // where条件
                // 订阅我的
                .eq(DiarySubscribeEntity::getSubscribeUserId, Oauth2Context.getUserIdElseThrow())
                // 我同意的
                .eq(DiarySubscribeEntity::getSubscribeStatus, "3"));
        // 为空返回
        if (CollUtil.isEmpty(entityList)) {
            return;
        }
        // 获取用户ID列表
        List<String> userIdList = entityList.stream().map(DiarySubscribeEntity::getCreateBy)
                .collect(Collectors.toList());
        // 根据用户ID列表查询邮箱列表
        List<String> mailList = baseMapper.queryMailByUserIdList(userIdList);
        if (CollUtil.isEmpty(mailList)) {
            return;
        }
        // 过滤非法的邮箱地址
        mailList = mailList.stream().filter(Validator::isEmail).collect(Collectors.toList());
        // 邮件内容
        String content = StrUtil.format("您订阅的UP主：{}更新啦!", Oauth2Context.getUsernameElseThrow());
        // 发送到邮箱
        mailNotifyService.sendText(mailList, "尺墨飞虹-Fantasy", content);
    }

    @Override
    public List<String> queryMySubscribeUserIdList() {
        // 查询订阅我的粉丝列表
        List<DiarySubscribeEntity> entityList = baseMapper.selectList(Wrappers.<DiarySubscribeEntity>lambdaQuery()
                // 只查询订阅用户的ID
                .select(DiarySubscribeEntity::getSubscribeUserId)
                // where条件
                // 我订阅的
                .eq(DiarySubscribeEntity::getCreateBy, Oauth2Context.getUserIdElseThrow())
                // 对方同意的
                .eq(DiarySubscribeEntity::getSubscribeStatus, "3"));
        // 获取用户ID列表
        return entityList.stream().map(DiarySubscribeEntity::getSubscribeUserId)
                .collect(Collectors.toList());
    }
}