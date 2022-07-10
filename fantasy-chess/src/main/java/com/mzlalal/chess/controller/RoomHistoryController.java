package com.mzlalal.chess.controller;

import cn.hutool.core.collection.CollUtil;
import com.mzlalal.base.entity.chess.dto.RoomHistoryEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.feign.chess.RoomHistoryFeignApi;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.chess.service.RoomHistoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 结算完成的房间controller
 *
 * @author Mzlalal
 * @date 2022-02-22 11:23:57
 */
@Api(tags = "结算完成的房间")
@RestController
@RequestMapping("/api/v1/chess/room.history")
public class RoomHistoryController implements RoomHistoryFeignApi {

    private final RoomHistoryService roomHistoryService;

    @Autowired
    public RoomHistoryController(RoomHistoryService roomHistoryService) {
        this.roomHistoryService = roomHistoryService;
    }

    @Override
    public Result<RoomHistoryEntity> list(@RequestBody Po<RoomHistoryEntity> po) {
        FantasyPage<RoomHistoryEntity> page = roomHistoryService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<RoomHistoryEntity> info(@PathVariable("id") Long id) {
        RoomHistoryEntity roomHistory = roomHistoryService.getById(id);
        return Result.ok(roomHistory);
    }

    @Override
    public Result<Void> save(@RequestBody RoomHistoryEntity roomHistory) {
        if (roomHistoryService.save(roomHistory)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody RoomHistoryEntity roomHistory) {
        if (roomHistoryService.updateById(roomHistory)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        if (roomHistoryService.removeByIds(CollUtil.newArrayList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }

}
