package com.mzlalal.oss.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.Page;
import com.mzlalal.notify.service.MailNotifyService;
import com.mzlalal.oss.dao.TodoNotifyDao;
import com.mzlalal.oss.service.TodoNotifyService;
import com.mzlalal.oss.service.todo.NotifyTypeVerifyEnum;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public TodoNotifyServiceImpl(MailNotifyService mailNotifyService) {
        this.mailNotifyService = mailNotifyService;
    }

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public Page<TodoNotifyEntity> queryPage(Po<TodoNotifyEntity> po) {
        // 查询参数
        QueryWrapper<TodoNotifyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("create_by", Oauth2Context.getUserIdElseThrow());
        // 排序
        wrapper.orderByDesc("notify_top_status");
        wrapper.orderByDesc("update_time");
        // 创建分页条件
        com.github.pagehelper.Page<TodoNotifyEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<TodoNotifyEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new Page<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    public void notifyTodoListCurrentTime() {
        // 当前年月日时分
        String currentTime = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        // 查询数据库
        List<TodoNotifyEntity> todoNotifyList = baseMapper.queryTodoListByCurrentTime(currentTime);
        // 为空则跳过
        if (CollUtil.isEmpty(todoNotifyList)) {
            return;
        }
        // 遍历
        todoNotifyList.parallelStream().forEach(item -> {
            // 发送到邮箱
            mailNotifyService.sendText(item.getNotifyMailSet(), "待办提醒-Fantasy", item.getNotifyMemo());
            // 获取下次执行时间
            NotifyTypeVerifyEnum.getEnum(item.getNotifyType()).checkAndGenerateNotifyNextTime(item);
        });
        // 批量更新
        this.saveOrUpdateBatch(todoNotifyList);
    }
}