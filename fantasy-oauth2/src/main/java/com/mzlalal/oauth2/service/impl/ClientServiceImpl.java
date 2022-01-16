package com.mzlalal.oauth2.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.ClientEntity;
import com.mzlalal.base.util.Page;
import com.mzlalal.oauth2.dao.ClientDao;
import com.mzlalal.oauth2.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * ServiceImpl
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:47
 */
@Service("clientServiceImpl")
public class ClientServiceImpl extends ServiceImpl<ClientDao, ClientEntity> implements ClientService {

    @Override
    public Page<ClientEntity> queryPage(Po<ClientEntity> po) {
        // 查询参数
        QueryWrapper<ClientEntity> wrapper = new QueryWrapper<>(po.getEntity());
        // 异步查询总行数 selectList一定要在future之后
        Future<Long> future = ThreadUtil.execAsync(() -> baseMapper.selectCount(wrapper));
        // 查询结果集
        List<ClientEntity> entityList = baseMapper.selectList(wrapper);
        // 获取总行数结果
        Long count = this.getTotalResult(future, log);
        // 返回结果
        return new Page<>(entityList, count, po.getPageInfo());
    }

    @Override
    public ClientEntity getOneByClientId(String clientId) {
        QueryWrapper<ClientEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id", clientId);
        return this.getOne(queryWrapper);
    }
}
