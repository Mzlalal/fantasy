package com.mzlalal.card.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.entity.card.dto.RoomEntity;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.Page;
import com.mzlalal.card.dao.RoomDao;
import com.mzlalal.card.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            wrapper.like("name" , name);
        }
        // 创建分页条件
        com.github.pagehelper.Page<RoomEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<RoomEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new Page<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeRoom(String roomId) {
        // 删除房间
        AssertUtil.isTrue(baseMapper.deleteById(roomId) > 0, "关闭房间失败,可能房间不存在");
    }
}
