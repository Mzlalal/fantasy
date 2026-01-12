package com.mzlalal.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.TodoCosmeticEntity;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oss.dao.TodoCosmeticDao;
import com.mzlalal.oss.service.TodoCosmeticService;
import java.util.List;
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
        wrapper.orderByDesc("cosmetic_top_status");
        wrapper.orderByDesc("update_time");
        // 创建分页条件
        Page<TodoCosmeticEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<TodoCosmeticEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }
}