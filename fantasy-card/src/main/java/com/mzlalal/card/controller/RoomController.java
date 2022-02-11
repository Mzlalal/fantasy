package com.mzlalal.card.controller;

import com.mzlalal.base.entity.card.dto.RoomEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.feign.card.RoomFeignApi;
import com.mzlalal.base.util.Page;
import com.mzlalal.card.service.RoomService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 房间controller
 *
 * @author Mzlalal
 * @date 2022-02-11 09:18:06
 */
@Api(tags = "房间")
@RestController
@RequestMapping("/api/v1/card/room")
public class RoomController implements RoomFeignApi {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public Result<RoomEntity> list(@RequestBody Po<RoomEntity> po) {
        Page<RoomEntity> page = roomService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<RoomEntity> info(@PathVariable("id") Long id) {
        RoomEntity room = roomService.getById(id);
        return Result.ok(room);
    }

    @Override
    public Result<Void> save(@RequestBody RoomEntity room) {
        if (roomService.save(room)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody RoomEntity room) {
        if (roomService.updateById(room)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody Long[] ids) {
        if (roomService.removeByIds(Collections.singletonList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }

}
