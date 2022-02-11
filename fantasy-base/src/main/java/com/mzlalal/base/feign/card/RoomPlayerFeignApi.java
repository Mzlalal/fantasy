package com.mzlalal.base.feign.card;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.card.dto.RoomPlayerEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
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
@FeignClient(contextId = "RoomPlayerFeignApi" , name = GlobalConstant.FANTASY_CARD, url = "${fantasy-card.feign.url}"
        , path = "/api/v1/card/room.player")
public interface RoomPlayerFeignApi {

    /**
     * 查询分页信息
     *
     * @param po 分页参数
     * @return Result
     */
    @ApiOperation("查看列表")
    @RequestMapping(value = "/list" , method = RequestMethod.POST)
    @ApiOperationSupport(order = 1)
    Result<RoomPlayerEntity> list(@RequestBody Po<RoomPlayerEntity> po);

    /**
     * 详情
     *
     * @param id ID 实体ID
     * @return Result
     */
    @ApiOperation("查看详情")
    @RequestMapping(value = "/info/{id}" , method = RequestMethod.GET)
    @ApiOperationSupport(order = 2)
    Result<RoomPlayerEntity> info(@PathVariable("id") Long id);

    /**
     * 保存
     *
     * @param roomPlayer 实体
     * @return Result
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    @ApiOperationSupport(order = 3)
    Result<Void> save(@RequestBody RoomPlayerEntity roomPlayer);

    /**
     * 更新
     *
     * @param roomPlayer 实体
     * @return Result
     */
    @ApiOperation("更新")
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    @ApiOperationSupport(order = 4)
    Result<Void> update(@RequestBody RoomPlayerEntity roomPlayer);

    /**
     * 根据ID数组批量删除
     *
     * @param ids ID数组
     * @return Result
     */
    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    @ApiOperationSupport(order = 5)
    Result<Void> delete(@RequestBody Long[] ids);
}
