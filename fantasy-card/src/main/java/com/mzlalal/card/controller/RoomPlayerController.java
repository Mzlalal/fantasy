package com.mzlalal.card.controller;

import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.card.req.TransferScoreReq;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.feign.card.RoomPlayerFeignApi;
import com.mzlalal.base.util.Page;
import com.mzlalal.card.service.RoomPlayerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 房间内的选手信息controller
 *
 * @author Mzlalal
 * @date 2022-02-10 17:11:39
 */
@Api(tags = "房间内的选手")
@RestController
@RequestMapping("/api/v1/card/room.player")
public class RoomPlayerController implements RoomPlayerFeignApi {

    private final RoomPlayerService roomPlayerService;

    @Autowired
    public RoomPlayerController(RoomPlayerService roomPlayerService) {
        this.roomPlayerService = roomPlayerService;
    }

    @Override
    public Result<RoomPlayerEntity> list(@RequestBody Po<RoomPlayerEntity> po) {
        Page<RoomPlayerEntity> page = roomPlayerService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<RoomPlayerEntity> info(@PathVariable("id") Long id) {
        RoomPlayerEntity roomPlayer = roomPlayerService.getById(id);
        return Result.ok(roomPlayer);
    }

    @Override
    public Result<Void> save(@RequestBody RoomPlayerEntity roomPlayer) {
        if (roomPlayerService.save(roomPlayer)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody RoomPlayerEntity roomPlayer) {
        if (roomPlayerService.updateById(roomPlayer)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody Long[] ids) {
        if (roomPlayerService.removeByIds(Collections.singletonList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> transferScore(@Validated @RequestBody TransferScoreReq transferScoreReq) {
        roomPlayerService.transferScore(transferScoreReq);
        return Result.ok();
    }

}
