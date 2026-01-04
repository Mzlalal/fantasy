package com.mzlalal.oss.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.TodoCosmeticEntity;
import com.mzlalal.base.entity.oss.vo.TodoNotifyVo;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.notify.service.MailNotifyService;
import com.mzlalal.oss.dao.TodoCosmeticDao;
import com.mzlalal.oss.enums.NotifyTypeEnum;
import com.mzlalal.oss.service.TodoCosmeticService;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 化妆品待办ServiceImpl
 *
 * @author Mzlalal
 * @date 2026-01-03 13:16:16
 */
@Service("todoCosmeticServiceImpl")
public class TodoCosmeticServiceImpl extends ServiceImpl<TodoCosmeticDao, TodoCosmeticEntity> implements TodoCosmeticService {

    /**
     * 邮件提醒service
     */
    private final MailNotifyService mailNotifyService;
    /**
     * string=>对象 redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;

    public TodoCosmeticServiceImpl(MailNotifyService mailNotifyService, RedisTemplate<String, Object> redisTemplate) {
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
    public FantasyPage<TodoCosmeticEntity> queryPage(Po<TodoCosmeticEntity> po) {
        // 查询参数
        QueryWrapper<TodoCosmeticEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("create_by", Oauth2Context.getUserIdElseThrow());
        // 排序
        wrapper.orderByDesc("notify_top_status");
        wrapper.orderByDesc("notify_exec_time");
        // 创建分页条件
        Page<TodoCosmeticEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<TodoCosmeticEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    public void notifyLazyModeTodoList() {
        // 重复提醒的key
        String notifyRepeatRedisKey = GlobalConstant.todoNotifyLazyMode();
        List<Object> todoNotifyList = redisTemplate.opsForList().range(notifyRepeatRedisKey, 0, Long.MAX_VALUE);
        // 判空
        if (CollUtil.isEmpty(todoNotifyList)) {
            return;
        }
        // 清空此key
        redisTemplate.delete(notifyRepeatRedisKey);
        // 遍历获取
        todoNotifyList.parallelStream().forEach(item -> {
            // 判断是否是TodoNotifyVo类型
            if (!(item instanceof TodoNotifyVo)) {
                return;
            }
            // 转换类型
            TodoNotifyVo todoNotifyVo = (TodoNotifyVo) item;
            // 邮箱标题
            String subject = StrUtil.format("化妆品-{}-懒人模式-Fantasy", todoNotifyVo.getNotifyMemo());
            // 发送到邮箱
            mailNotifyService.sendText(todoNotifyVo.getNotifyMailSet(), subject, todoNotifyVo.getNotifyMemo());
            // 更新次数
            int notifyLazyModeTimes = todoNotifyVo.getNotifyLazyModeTimes() - 1;
            // 如果提醒次数仍然大于0,则加入到集合中
            if (notifyLazyModeTimes > 0) {
                // 更新次数
                todoNotifyVo.setNotifyLazyModeTimes(notifyLazyModeTimes);
                // 保存到redis
                redisTemplate.opsForList().rightPush(notifyRepeatRedisKey, todoNotifyVo);
            }
        });
    }

    @Override
    public void notifyCurrentTimeTodoList() {
        QueryWrapper<TodoCosmeticEntity> queryWrapper = new QueryWrapper<>();
        // 当前年月日时分
        String currentTime = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        // 小于等于当前执行时间
        queryWrapper.le("notify_exec_time", currentTime);
        // 查询数据库
        List<TodoCosmeticEntity> todoNotifyList = baseMapper.selectList(queryWrapper);
        // 为空则跳过
        if (CollUtil.isEmpty(todoNotifyList)) {
            return;
        }
        // 迭代遍历
        Iterator<TodoCosmeticEntity> iterator = todoNotifyList.iterator();
        while (iterator.hasNext()) {
            TodoCosmeticEntity todoNotify = iterator.next();
            // 邮箱标题
            String subject = StrUtil.format("化妆品-{}-Fantasy", todoNotify.getNotifyMemo());
            // 发送到邮箱
            mailNotifyService.sendText(todoNotify.getNotifyMailSet(), subject, todoNotify.getNotifyMemo());
            // 重复提醒的存放在redis列表中
            if (todoNotify.getNotifyLazyModeTimes() > 0) {
                TodoNotifyVo todoNotifyVo = new TodoNotifyVo();
                BeanUtil.copyProperties(todoNotify, todoNotifyVo);
                redisTemplate.opsForList().rightPush(GlobalConstant.todoNotifyLazyMode(), todoNotifyVo);
            }
            // 提醒后删除
            if (StrUtil.equals(GlobalConstant.STATUS_ONE, todoNotify.getNotifyAfterDelete())) {
                // 逻辑删除
                this.removeById(todoNotify.getId());
                // 去除
                iterator.remove();
            }
            // 提醒类型
            String notifyType = todoNotify.getNotifyType();
            // 等于0,只提醒一次
            if (StrUtil.equals(GlobalConstant.STATUS_ZERO, notifyType)) {
                // 把下次执行时间推迟到未来
                todoNotify.setNotifyExecTime(DateUtil.parse("9999-12-31 23:59:59"));
                continue;
            }
            // 生成下次执行时间
            NotifyTypeEnum.getEnum(notifyType).createNextTime(todoNotify.getNotifyExecTime(), todoNotify);
        }
        // 批量更新
        this.saveOrUpdateBatch(todoNotifyList);
    }
}