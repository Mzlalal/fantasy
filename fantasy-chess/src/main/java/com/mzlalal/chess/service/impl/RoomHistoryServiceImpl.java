package com.mzlalal.chess.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.entity.chess.dto.RoomHistoryEntity;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.Page;
import com.mzlalal.chess.dao.RoomHistoryDao;
import com.mzlalal.chess.service.RoomHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 结算完成的房间ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-02-22 11:23:57
 */
@Service("roomHistoryServiceImpl")
public class RoomHistoryServiceImpl extends ServiceImpl<RoomHistoryDao, RoomHistoryEntity> implements RoomHistoryService {

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public Page<RoomHistoryEntity> queryPage(Po<RoomHistoryEntity> po) {
        // 查询参数
        QueryWrapper<RoomHistoryEntity> wrapper = new QueryWrapper<>();
        // 根据用户ID查询
        String userId = Oauth2Context.getUserId();
        wrapper.apply(StrUtil.isNotBlank(userId), "FIND_IN_SET('" + userId + "', player_id_set)");
        // 创建分页条件
        com.github.pagehelper.Page<RoomHistoryEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<RoomHistoryEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new Page<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }
}
