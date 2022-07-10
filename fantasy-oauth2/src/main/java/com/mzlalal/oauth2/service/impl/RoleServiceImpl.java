package com.mzlalal.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.RoleEntity;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oauth2.dao.RoleDao;
import com.mzlalal.oauth2.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-02-28 10:39:10
 */
@Service("roleServiceImpl")
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public FantasyPage<RoleEntity> queryPage(Po<RoleEntity> po) {
        // 查询参数
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>(po.getEntity());
        // 创建分页条件
        Page<RoleEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<RoleEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    public List<VueSelect> queryRoleVueSelectList() {
        return baseMapper.queryRoleVueSelectList();
    }
}
