package com.mzlalal.card.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.card.dto.RoomEntity;
import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.card.req.PlayerHistoryMessageReq;
import com.mzlalal.base.entity.card.req.TransferScoreReq;
import com.mzlalal.base.entity.global.WsResult;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.Page;
import com.mzlalal.card.dao.RoomPlayerDao;
import com.mzlalal.card.service.RoomPlayerService;
import com.mzlalal.card.service.RoomService;
import com.mzlalal.card.service.websocket.session.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 房间内的选手ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-02-10 17:11:39
 */
@Service("roomPlayerServiceImpl")
public class RoomPlayerServiceImpl extends ServiceImpl<RoomPlayerDao, RoomPlayerEntity> implements RoomPlayerService {
    /**
     * 房间操作
     */
    private final RoomService roomService;
    /**
     * websocket用户会话操作
     */
    private UserSessionService userSessionService;
    /**
     * string=>对象 redis操作模板
     */
    private final RedisTemplate<String, WsResult<Void>> redisTemplate;

    public RoomPlayerServiceImpl(RoomService roomService, RedisTemplate<String, WsResult<Void>> redisTemplate) {
        this.roomService = roomService;
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void transferScore(@Validated TransferScoreReq transferScoreReq) {
        String roomId = transferScoreReq.getRoomId();
        String from = transferScoreReq.getFrom();
        String to = transferScoreReq.getTo();
        Integer change = transferScoreReq.getChange();

        // 检查发起人状态
        RoomPlayerEntity fromPlayer = this.getOneByRoomIdAndUserId(roomId, from);
        AssertUtil.equals(fromPlayer.getPlayerStatus(), GlobalConstant.STATUS_ON, GlobalResult.SUB_PLAYER_STATUS_OFF);

        // 检查接收人状态
        RoomPlayerEntity toPlayer = this.getOneByRoomIdAndUserId(roomId, to);
        AssertUtil.equals(toPlayer.getPlayerStatus(), GlobalConstant.STATUS_ON, GlobalResult.ADD_PLAYER_STATUS_OFF);

        // 扣除发起人分数
        int sub = baseMapper.subPlayerScore(roomId, from, change);
        AssertUtil.isTrue(sub > 0, GlobalResult.SUB_SCORE_FAIL);

        // 增加接收人分数
        int add = baseMapper.addPlayerScore(roomId, to, change);
        AssertUtil.isTrue(add > 0, GlobalResult.ADD_SCORE_FAIL);

        // 发送消息到房间
        userSessionService.broadcast(roomId, from, transferScoreReq.getMessage());
    }

    @Override
    public List<WsResult<Void>> queryRoomPlayerHistoryMessage(PlayerHistoryMessageReq req) {
        // 获取成员
        Set<WsResult<Void>> members = redisTemplate.opsForSet().members(req.getRoomId());
        // 为空返回空集合
        if (CollUtil.isEmpty(members)) {
            return CollUtil.newArrayList();
        }
        // 过滤from为当前选手ID的集合
        return members.parallelStream()
                .filter(item -> StrUtil.equals(item.getFrom(), req.getFrom()))
                .collect(Collectors.toList());
    }
}
