package com.mzlalal.oss.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import com.mzlalal.base.entity.oss.vo.TodoNotifyVo;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.Page;
import com.mzlalal.notify.service.MailNotifyService;
import com.mzlalal.oss.dao.TodoNotifyDao;
import com.mzlalal.oss.service.TodoNotifyService;
import com.mzlalal.oss.service.todo.NotifyTypeEnum;
import org.springframework.data.redis.core.RedisTemplate;
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
    public void notifyLazyModeTodoList() {
        // 重复提醒的key
        String notifyRepeatRedisKey = GlobalConstant.todoNotifyRepeatRedisKey();
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
            // 发送到邮箱
            mailNotifyService.sendText(todoNotifyVo.getNotifyMailSet(), "待办提醒-懒人模式-Fantasy", todoNotifyVo.getNotifyMemo());
            // 更新次数
            int notifyLazyModeTimes = Integer.parseInt(todoNotifyVo.getNotifyLazyModeTimes()) - 1;
            // 如果提醒次数仍然大于0,则加入到集合中
            if (notifyLazyModeTimes > 0) {
                // 更新次数
                todoNotifyVo.setNotifyLazyModeTimes(String.valueOf(notifyLazyModeTimes));
                // 保存到redis
                redisTemplate.opsForList().rightPush(notifyRepeatRedisKey, todoNotifyVo);
            }
        });
    }

    @Override
    public void notifyCurrentTimeTodoList() {
        QueryWrapper<TodoNotifyEntity> queryWrapper = new QueryWrapper<>();
        // 当前年月日时分
        String currentTime = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        queryWrapper.eq("notify_next_time", currentTime);
        // 逻辑删除
        queryWrapper.eq("is_hide", GlobalConstant.STATUS_OFF);
        // 查询数据库
        List<TodoNotifyEntity> todoNotifyList = baseMapper.selectList(queryWrapper);
        // 为空则跳过
        if (CollUtil.isEmpty(todoNotifyList)) {
            return;
        }
        // 遍历
        todoNotifyList.parallelStream().forEach(item -> {
            // 发送到邮箱
            mailNotifyService.sendText(item.getNotifyMailSet(), "待办提醒-Fantasy", item.getNotifyMemo());
            // 重复提醒的存放在redis列表中
            if (Integer.parseInt(item.getNotifyLazyModeTimes()) > 0) {
                TodoNotifyVo todoNotifyVo = new TodoNotifyVo();
                BeanUtil.copyProperties(item, todoNotifyVo);
                redisTemplate.opsForList().rightPush(GlobalConstant.todoNotifyRepeatRedisKey(), todoNotifyVo);
            }
            // 提醒后删除
            if (StrUtil.equals(GlobalConstant.STATUS_ON, item.getNotifyAfterDelete())) {
                item.setIsHide(Integer.parseInt(GlobalConstant.STATUS_ON));
            }
            // 是否重复提醒
            if (StrUtil.equals(GlobalConstant.STATUS_OFF, item.getNotifyType())) {
                return;
            }
            // 获取下次执行时间
            NotifyTypeEnum.getEnum(item.getNotifyType()).generateNotifyNextTime(item);
        });
        // 批量更新
        this.saveOrUpdateBatch(todoNotifyList);
    }
}
