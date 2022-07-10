package com.mzlalal.chess.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.chess.dto.RoomHistoryEntity;
import com.mzlalal.base.entity.chess.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.chess.vo.HistoryMessageVo;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.base.util.LambdaUtil;
import com.mzlalal.chess.dao.RoomPlayerDao;
import com.mzlalal.chess.service.RoomHistoryService;
import com.mzlalal.chess.service.RoomPlayerService;
import com.mzlalal.chess.service.RoomService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
     * 历史房间操作
     */
    private final RoomHistoryService roomHistoryService;
    /**
     * string=>HistoryMessageVo redis操作模板
     */
    private final RedisTemplate<String, HistoryMessageVo> redisTemplate;

    public RoomPlayerServiceImpl(RoomService roomService, RoomHistoryService roomHistoryService
            , RedisTemplate<String, HistoryMessageVo> redisTemplate) {
        this.roomService = roomService;
        this.roomHistoryService = roomHistoryService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public FantasyPage<RoomPlayerEntity> queryPage(Po<RoomPlayerEntity> po) {
        // 查询参数
        QueryWrapper<RoomPlayerEntity> wrapper = new QueryWrapper<>(po.getEntity());
        // 创建分页条件
        Page<RoomPlayerEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<RoomPlayerEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }


    @Override
    public RoomPlayerEntity queryOneByRoomIdAndUserId(String roomId, String userId) {
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
    public List<RoomPlayerEntity> queryRoomPlayerListByRoomId(String roomId) {
        // 使用roomId查询
        RoomPlayerEntity queryEntity = RoomPlayerEntity.builder()
                .roomId(roomId)
                .build();
        QueryWrapper<RoomPlayerEntity> queryWrapper = new QueryWrapper<>(queryEntity);

        // 查询
        return this.list(queryWrapper);
    }

    @Override
    public int addRoomPlayerScore(String roomId, String userId, Integer change) {
        return baseMapper.addRoomPlayerScore(roomId, userId, change);
    }

    @Override
    public int subRoomPlayerScore(String roomId, String userId, Integer change) {
        return baseMapper.subRoomPlayerScore(roomId, userId, change);
    }

    @Override
    public void updateRoomPlayerStatus(String roomId, String userId, String status) {
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
    public List<HistoryMessageVo> queryPlayerHistoryMessage(String roomId) {
        // 用户ID
        String userId = Oauth2Context.getUserIdElseThrow();
        // 获取房间内的选手
        String redisKey = GlobalConstant.messageInRoom(roomId);
        Set<HistoryMessageVo> historyMessageVoSet = redisTemplate.opsForSet().members(redisKey);
        // 为空返回空集合
        List<HistoryMessageVo> historyList = CollUtil.newArrayList();
        if (CollUtil.isEmpty(historyMessageVoSet)) {
            return historyList;
        }
        // 遍历过滤
        for (HistoryMessageVo historyMessageVo : historyMessageVoSet) {
            // 过滤from为当前选手ID的集合
            if (StrUtil.equalsAny(userId, historyMessageVo.getFrom(), historyMessageVo.getTo())) {
                historyList.add(historyMessageVo);
            }
        }
        // 根据时间排序
        historyList = historyList.stream()
                .sorted(Comparator.comparing(HistoryMessageVo::getTime))
                .collect(Collectors.toList());
        // 翻转
        return historyList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeRoom(String[] ids) {
        // 房间ID集合
        AssertUtil.notEmpty(ids, "房间ID集合不能为空");
        ArrayList<String> roomIdList = CollUtil.newArrayList(ids);

        for (String id : ids) {
            // 查询房间内的选手信息
            List<RoomPlayerEntity> playerList = this.queryRoomPlayerListByRoomId(id);
            if (CollUtil.isEmpty(playerList)) {
                continue;
            }
            // 获取用户ID作为字符串
            String playerIdStr = LambdaUtil.getFieldStr(playerList, RoomPlayerEntity::getId);
            // 保存房间历史数据
            RoomHistoryEntity entity = RoomHistoryEntity.builder()
                    .roomName(CollUtil.getFirst(playerList).getRoomName())
                    .playerIdSet(playerIdStr)
                    .playerStatusSet(playerList)
                    .build();
            AssertUtil.isTrue(roomHistoryService.save(entity), "房间结算失败,可能房间不存在");
        }

        // 删除房间内的所有选手
        QueryWrapper<RoomPlayerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("room_id", roomIdList);
        AssertUtil.isTrue(baseMapper.delete(queryWrapper) > 0, "关闭房间失败,可能房间不存在");

        // 删除房间
        roomService.removeByIds(roomIdList);

        // 删除房间redis消息
        for (String roomId : roomIdList) {
            String redisKey = GlobalConstant.messageInRoom(roomId);
            redisTemplate.delete(redisKey);
        }
    }
}
