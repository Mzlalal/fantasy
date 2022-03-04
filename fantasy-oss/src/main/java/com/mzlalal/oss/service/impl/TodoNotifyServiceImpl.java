package com.mzlalal.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import com.mzlalal.base.util.Page;
import com.mzlalal.oss.dao.TodoNotifyDao;
import com.mzlalal.oss.service.TodoNotifyService;
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
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public Page<TodoNotifyEntity> queryPage(Po<TodoNotifyEntity> po) {
        // 查询参数
        QueryWrapper<TodoNotifyEntity> wrapper = new QueryWrapper<>(po.getEntity());
        // 创建分页条件
        com.github.pagehelper.Page<TodoNotifyEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<TodoNotifyEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new Page<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }
}