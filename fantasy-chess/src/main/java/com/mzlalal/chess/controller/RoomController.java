package com.mzlalal.chess.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.chess.dto.RoomEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.feign.chess.RoomFeignApi;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.Page;
import com.mzlalal.chess.service.RoomPlayerService;
import com.mzlalal.chess.service.RoomService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 房间controller
 *
 * @author Mzlalal
 * @date 2022-02-11 09:18:06
 */
@Api(tags = "房间")
@RestController
@RequestMapping("/api/v1/chess/room")
public class RoomController implements RoomFeignApi {

    private final RoomService roomService;

    private final RoomPlayerService roomPlayerService;

    @Autowired
    public RoomController(RoomService roomService, RoomPlayerService roomPlayerService) {
        this.roomService = roomService;
        this.roomPlayerService = roomPlayerService;
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
    public Result<RoomEntity> save() {
        // 用户名
        String username = Oauth2Context.getUsernameElseThrow();
        // 用户ID
        String userId = Oauth2Context.getUserIdElseThrow();
        // 判断房间是否存在
        RoomEntity existRoom = roomService.getById(userId);
        // 存在直接返回
        if (existRoom != null) {
            return GlobalResult.ROOM_EXIST.result(existRoom);
        }
        // 房间号
        int code = RandomUtil.randomInt(1000, 9999);
        // 格式房间名
        String roomName = StrUtil.format(GlobalConstant.USERNAME_DISPLAY + "</span>（{}）的房间"
                , username, code);
        // 创建房间
        RoomEntity room = RoomEntity.builder().id(userId).name(roomName).code(code).build();
        // 报错到数据库
        if (roomService.save(room)) {
            return Result.ok(room);
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
    public Result<Void> delete(@RequestBody String[] ids) {
        roomPlayerService.closeRoom(ids);
        return Result.ok();
    }

}
