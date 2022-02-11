package com.mzlalal.card.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.card.dto.RoomEntity;
import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.Page;
import com.mzlalal.card.dao.RoomPlayerDao;
import com.mzlalal.card.service.RoomPlayerService;
import com.mzlalal.card.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 房间内的选手ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-02-10 17:11:39
 */
@Service("roomPlayerServiceImpl")
public class RoomPlayerServiceImpl extends ServiceImpl<RoomPlayerDao, RoomPlayerEntity> implements RoomPlayerService {
    /**
     * 房间服务
     */
    private final RoomService roomService;

    public RoomPlayerServiceImpl(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public Page<RoomPlayerEntity> queryPage(Po<RoomPlayerEntity> po) {
        // 查询参数
        QueryWrapper<RoomPlayerEntity> wrapper = new QueryWrapper<>(po.getEntity());
        // 异步查询总行数 selectList一定要在future之后
        Future<Long> future = ThreadUtil.execAsync(() -> baseMapper.selectCount(wrapper));
        // 查询结果集
        List<RoomPlayerEntity> entityList = baseMapper.selectList(wrapper);
        // 获取总行数结果
        Long count = this.getTotalResult(future, log);
        // 返回结果
        return new Page<>(entityList, count, po.getPageInfo());
    }


    @Override
    public RoomPlayerEntity getOneByRoomIdAndUserId(String roomId, String userId) {
        // 使用roomId, userId查询是否已经上桌过
        RoomPlayerEntity queryEntity = RoomPlayerEntity.builder()
                .roomId(roomId)
                .id(userId)
                .build();
        QueryWrapper<RoomPlayerEntity> queryWrapper = new QueryWrapper<>(queryEntity);

        // 查询
        return this.getOne(queryWrapper);
    }

    @Override
    public RoomPlayerEntity initPlayer(String roomId, UserEntity user) {
        // 校验用户
        AssertUtil.notNull(user, "用户不存在");
        String userId = user.getId();
        // 检验房间
        RoomEntity room = roomService.getById(roomId);
        AssertUtil.notNull(room, "房间不存在");

        // 判断是否历史上桌
        RoomPlayerEntity existPlayer = this.getOneByRoomIdAndUserId(roomId, userId);
        if (existPlayer != null) {
            // 下桌,重新上桌
            if (GlobalConstant.STATUS_OFF.equals(existPlayer.getPlayerStatus())) {
                existPlayer.setPlayerStatus(GlobalConstant.STATUS_ON);
                // 更新数据库状态
                this.updatePlayerStatus(roomId, userId, GlobalConstant.STATUS_ON);
            }
            return existPlayer;
        }

        // 未上桌,初始化选手信息
        RoomPlayerEntity roomPlayer = RoomPlayerEntity.builder()
                .id(userId)
                .playerName(user.getUsername())
                .playerScore(0)
                .playerStatus(GlobalConstant.STATUS_ON)
                .roomId(roomId)
                .roomName(room.getName())
                .build();

        // 保存选手信息
        AssertUtil.isTrue(this.save(roomPlayer), "加入房间失败,请稍后再试");
        return roomPlayer;
    }

    @Override
    public List<RoomPlayerEntity> queryRoomPlayerByRoomId(String roomId) {
        // 使用roomId查询
        RoomPlayerEntity queryEntity = RoomPlayerEntity.builder()
                .roomId(roomId)
                .build();
        QueryWrapper<RoomPlayerEntity> queryWrapper = new QueryWrapper<>(queryEntity);

        // 查询
        return this.list(queryWrapper);
    }

    @Override
    public void updatePlayerStatus(String roomId, String userId, String status) {
        // where条件房间ID与用户ID
        RoomPlayerEntity updateEntity = RoomPlayerEntity.builder()
                .roomId(roomId)
                .id(userId)
                .build();
        UpdateWrapper<RoomPlayerEntity> updateWrapper = new UpdateWrapper<>(updateEntity);

        // 需要更新的值
        RoomPlayerEntity entity = RoomPlayerEntity.builder().playerStatus(status).build();
        // 更新
        AssertUtil.isTrue(this.update(entity, updateWrapper), "更新用户状态失败,可能未加入房间");
    }
}
