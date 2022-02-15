package com.mzlalal.card.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.entity.card.dto.RoomEntity;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.util.Page;
import com.mzlalal.card.dao.RoomDao;
import com.mzlalal.card.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 房间ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-02-11 09:18:06
 */
@Service("roomServiceImpl")
public class RoomServiceImpl extends ServiceImpl<RoomDao, RoomEntity> implements RoomService {

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public Page<RoomEntity> queryPage(Po<RoomEntity> po) {
        // 查询参数
        QueryWrapper<RoomEntity> wrapper = new QueryWrapper<>();
        // 房间名
        String name = po.getEntity().getName();
        if (StrUtil.isNotBlank(name)) {
            wrapper.like("name", name);
        }
        // 异步查询总行数 selectList一定要在future之后
        Future<Long> future = ThreadUtil.execAsync(() -> baseMapper.selectCount(wrapper));
        // 查询结果集
        List<RoomEntity> entityList = baseMapper.selectList(wrapper);
        // 获取总行数结果
        Long count = this.getTotalResult(future, log);
        // 返回结果
        return new Page<>(entityList, count, po.getPageInfo());
    }
}