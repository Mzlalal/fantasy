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
import com.mzlalal.base.entity.card.req.PlayerOutOrJoinRoomReq;
import com.mzlalal.base.entity.card.req.TransferScoreReq;
import com.mzlalal.base.entity.card.vo.HistoryMessageVo;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.Page;
import com.mzlalal.card.dao.RoomPlayerDao;
import com.mzlalal.card.service.RoomPlayerService;
import com.mzlalal.card.service.RoomService;
import com.mzlalal.card.service.websocket.session.UserSessionService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * redisson数据源
     */
    private final RedissonClient redissonClient;

    public RoomPlayerServiceImpl(RoomService roomService, RedisTemplate<String, Object> redisTemplate
            , RedissonClient redissonClient) {
        this.roomService = roomService;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
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
    public void playerInit(String roomId, UserEntity user) {
        // 校验用户
        AssertUtil.notNull(user, "用户不存在");
        String userId = user.getId();
        String username = user.getUsername();
        // 检验房间
        RoomEntity room = roomService.getById(roomId);
        AssertUtil.notNull(room, "房间不存在");

        // 是否已上桌
        RoomPlayerEntity existPlayer = this.getOneByRoomIdAndUserId(roomId, userId);
        if (existPlayer != null) {
            // 发送房间内的选手状态
            userSessionService.sendRoomPlayerMessage(roomId);
            return;
        }

        // 未上桌,初始化选手信息
        RoomPlayerEntity roomPlayer = RoomPlayerEntity.builder()
                .id(userId)
                .playerName(username)
                .playerScore(0)
                .playerStatus(GlobalConstant.STATUS_ON)
                .roomId(roomId)
                .roomName(room.getName())
                .build();
        AssertUtil.isTrue(this.save(roomPlayer), "上桌失败,请稍后再试");

        // 上桌消息
        PlayerOutOrJoinRoomReq req = PlayerOutOrJoinRoomReq.builder()
                .roomId(roomId)
                .userId(userId)
                .username(username)
                .build();
        this.playerJoinRoom(req);
    }

    @Override
    public void playerJoinRoom(PlayerOutOrJoinRoomReq req) {
        // 用户ID
        String userId = req.getUserId();
        // 房间ID
        String roomId = req.getRoomId();

        // 用户退出房间
        this.updateStatus(roomId, userId, GlobalConstant.STATUS_ON);

        // 上桌消息
        String message = StrUtil.format(GlobalConstant.USERNAME_DISPLAY + "上桌了", req.getUsername());
        // 广播消息
        userSessionService.broadcast(roomId, userId, message);
    }

    @Override
    public void playerOutRoom(@Validated PlayerOutOrJoinRoomReq req) {
        // 用户ID
        String userId = req.getUserId();
        // 房间ID
        String roomId = req.getRoomId();

        // 用户退出房间
        this.updateStatus(roomId, userId, GlobalConstant.STATUS_OFF);

        // 下桌消息
        String message = StrUtil.format(GlobalConstant.USERNAME_DISPLAY + "下桌了", req.getUsername());
        // 广播消息
        userSessionService.broadcast(roomId, userId, message);
    }

    @Override
    public List<RoomPlayerEntity> getRoomPlayerListByRoomId(String roomId) {
        // 使用roomId查询
        RoomPlayerEntity queryEntity = RoomPlayerEntity.builder()
                .roomId(roomId)
                .build();
        QueryWrapper<RoomPlayerEntity> queryWrapper = new QueryWrapper<>(queryEntity);

        // 查询
        return this.list(queryWrapper);
    }

    @Override
    public void updateStatus(String roomId, String userId, String status) {
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
    public void transferScore(@Validated TransferScoreReq req) {
        String roomId = req.getRoomId();
        String from = req.getFrom();
        String to = req.getTo();
        Integer change = req.getChange();

        String lockRedisKey = GlobalConstant.transferLockRedisKey(roomId);
        RLock lock = redissonClient.getLock(lockRedisKey);
        try {
            // 获取锁
            lock.lock();

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
            userSessionService.broadcast(roomId, from, to, req.getMessage());
        } finally {
            // 解锁
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    @Override
    public List<HistoryMessageVo> queryPlayerHistoryMessage(String roomId) {
        // 用户ID
        String userId = Oauth2Context.getUserId();
        // 获取房间内的选手
        String redisKey = GlobalConstant.roomMessageRedisKey(roomId);
        Set<Object> memberSet = redisTemplate.opsForSet().members(redisKey);
        // 刷新过期时间,一天过期
        redisTemplate.expire(redisKey, 1, TimeUnit.DAYS);
        // 为空返回空集合
        List<HistoryMessageVo> historyList = CollUtil.newArrayList();
        if (CollUtil.isEmpty(memberSet)) {
            return historyList;
        }
        // 遍历过滤
        for (Object member : memberSet) {
            if (member instanceof HistoryMessageVo) {
                // 强转类型
                HistoryMessageVo historyMessageVo = (HistoryMessageVo) member;
                // 过滤from为当前选手ID的集合
                if (StrUtil.equalsAny(userId, historyMessageVo.getFrom(), historyMessageVo.getTo())) {
                    historyList.add(historyMessageVo);
                }
            }
        }
        return historyList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeRoom(String[] ids) {
        // 房间ID集合
        AssertUtil.notEmpty(ids, "房间ID集合不能为空");
        ArrayList<String> roomIdList = CollUtil.newArrayList(ids);

        // 删除房间内的所有选手
        QueryWrapper<RoomPlayerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("room_id", roomIdList);
        AssertUtil.isTrue(baseMapper.delete(queryWrapper) > 0, "关闭房间失败,可能房间不存在");

        // 删除房间
        roomService.removeByIds(roomIdList);

        // 删除房间redis消息
        for (String roomId : roomIdList) {
            String redisKey = GlobalConstant.roomMessageRedisKey(roomId);
            redisTemplate.delete(redisKey);
        }
    }
}