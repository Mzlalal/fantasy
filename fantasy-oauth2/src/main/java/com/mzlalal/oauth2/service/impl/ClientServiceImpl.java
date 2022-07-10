package com.mzlalal.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.ClientEntity;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oauth2.dao.ClientDao;
import com.mzlalal.oauth2.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户端ServiceImpl
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:47
 */
@Service("clientServiceImpl")
public class ClientServiceImpl extends ServiceImpl<ClientDao, ClientEntity> implements ClientService {

    @Override
    public FantasyPage<ClientEntity> queryPage(Po<ClientEntity> po) {
        // 查询参数
        QueryWrapper<ClientEntity> wrapper = new QueryWrapper<>(po.getEntity());
        // 创建分页条件
        Page<ClientEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<ClientEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    public ClientEntity getOneByClientKey(String clientKey) {
        QueryWrapper<ClientEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_key", clientKey);
        return this.getOne(queryWrapper);
    }
}
