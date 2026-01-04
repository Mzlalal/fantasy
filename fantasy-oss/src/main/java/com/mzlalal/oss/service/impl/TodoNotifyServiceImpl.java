package com.mzlalal.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.notify.service.MailNotifyService;
import com.mzlalal.oss.dao.TodoNotifyDao;
import com.mzlalal.oss.service.TodoNotifyService;
import java.util.List;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 待办提醒ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-03-04 21:58:11
 */
@Service("todoNotifyServiceImpl")
public class TodoNotifyServiceImpl extends ServiceImpl<TodoNotifyDao, TodoNotifyEntity> implements TodoNotifyService {

    /**
     * 邮件提醒service
     */
    private final MailNotifyService mailNotifyService;
    /**
     * string=>对象 redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;

    public TodoNotifyServiceImpl(MailNotifyService mailNotifyService, RedisTemplate<String, Object> redisTemplate) {
        this.mailNotifyService = mailNotifyService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public FantasyPage<TodoNotifyEntity> queryPage(Po<TodoNotifyEntity> po) {
        // 查询参数
        QueryWrapper<TodoNotifyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("create_by", Oauth2Context.getUserIdElseThrow());
        // 排序
        wrapper.orderByDesc("notify_top_status");
        wrapper.orderByDesc("notify_exec_time");
        // 创建分页条件
        Page<TodoNotifyEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<TodoNotifyEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }
}
