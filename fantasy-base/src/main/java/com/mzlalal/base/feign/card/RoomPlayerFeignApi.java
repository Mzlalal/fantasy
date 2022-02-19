package com.mzlalal.base.feign.card;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.card.req.PlayerOutOrJoinRoomReq;
import com.mzlalal.base.entity.card.req.TransferScoreReq;
import com.mzlalal.base.entity.card.vo.HistoryMessageVo;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 房间内的选手信息feign调用类
 *
 * @author Mzlalal
 * @date 2022-02-10 17:11:39
 **/
@FeignClient(contextId = "RoomPlayerFeignApi", name = GlobalConstant.FANTASY_CARD, url = "${fantasy-card.feign.url}"
        , path = "/api/v1/card/room.player")
public interface RoomPlayerFeignApi {

    /**
     * 查询分页信息
     *
     * @param po 分页参数
     * @return Result
     */
    @ApiOperation("查看列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 10)
    Result<RoomPlayerEntity> list(@RequestBody Po<RoomPlayerEntity> po);

    /**
     * 详情
     *
     * @param id ID 实体ID
     * @return Result
     */
    @ApiOperation("查看详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 20)
    Result<RoomPlayerEntity> info(@PathVariable("id") Long id);

    /**
     * 保存
     *
     * @param roomPlayer 实体
     * @return Result
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 30)
    Result<Void> save(@RequestBody RoomPlayerEntity roomPlayer);

    /**
     * 更新
     *
     * @param roomPlayer 实体
     * @return Result
     */
    @ApiOperation("更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 40)
    Result<Void> update(@RequestBody RoomPlayerEntity roomPlayer);

    /**
     * 根据ID数组批量删除
     *
     * @param ids ID数组
     * @return Result
     */
    @ApiOperation("删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 50)
    Result<Void> delete(@RequestBody String[] ids);

    /**
     * 房间内的选手下桌请求
     *
     * @param req 房间内的选手下桌请求
     * @return Result
     */
    @ApiOperation("房间内的选手下桌")
    @RequestMapping(value = "/player.join.room", method = RequestMethod.POST)
    Result<Void> playerJoinRoom(@RequestBody PlayerOutOrJoinRoomReq req);

    /**
     * 房间内的选手下桌请求
     *
     * @param req 房间内的选手下桌请求
     * @return Result
     */
    @ApiOperation("房间内的选手下桌")
    @RequestMapping(value = "/player.out.room", method = RequestMethod.POST)
    Result<Void> playerOutRoom(@RequestBody PlayerOutOrJoinRoomReq req);

    /**
     * 转账分数
     *
     * @param req 转账分数请求
     * @return Result
     */
    @ApiOperation("转账分数")
    @RequestMapping(value = "/transfer.score", method = RequestMethod.POST)
    Result<Void> transferScore(@RequestBody TransferScoreReq req);

    /**
     * 根据房间ID,选手ID查询历史消息
     *
     * @return List<WsResult>
     */
    @ApiOperation("查询房间选手的历史消息")
    @RequestMapping(value = "/query.room.player.history.message", method = RequestMethod.POST)
    Result<HistoryMessageVo> queryRoomPlayerHistoryMessage();
}
