package com.mzlalal.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.RoleClientEntity;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oauth2.dao.RoleClientDao;
import com.mzlalal.oauth2.service.RoleClientService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色与客户端的关联关系ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-02-28 22:48:43
 */
@Service("roleClientServiceImpl")
public class RoleClientServiceImpl extends ServiceImpl<RoleClientDao, RoleClientEntity> implements RoleClientService {

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public FantasyPage<RoleClientEntity> queryPage(Po<RoleClientEntity> po) {
        // 查询参数
        QueryWrapper<RoleClientEntity> wrapper = new QueryWrapper<>(po.getEntity());
        // 创建分页条件
        Page<RoleClientEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<RoleClientEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }
}