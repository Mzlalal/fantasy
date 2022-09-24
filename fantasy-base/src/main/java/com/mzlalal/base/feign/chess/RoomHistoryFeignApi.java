package com.mzlalal.base.feign.chess;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.chess.dto.RoomHistoryEntity;
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
 * 结算完成的房间feign调用类
 *
 * @author Mzlalal
 * @date 2022-02-22 11:23:57
 **/
@FeignClient(contextId = "RoomHistoryFeignApi" , name = GlobalConstant.FANTASY_CHESS, url = "${fantasy-chess.feign.url:}"
        , path = "/api/v1/chess/room.history")
public interface RoomHistoryFeignApi {

    /**
     * 查询分页信息
     *
     * @param po 分页参数
     * @return Result
     */
    @ApiOperation("查看列表")
    @RequestMapping(value = "/list" , method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 10)
    Result<RoomHistoryEntity> list(@RequestBody Po<RoomHistoryEntity> po);

    /**
     * 详情
     *
     * @param id ID 实体ID
     * @return Result
     */
    @ApiOperation("查看详情")
    @RequestMapping(value = "/info/{id}" , method = RequestMethod.GET)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 20)
    Result<RoomHistoryEntity> info(@PathVariable("id") Long id);

    /**
     * 保存
     *
     * @param roomHistory 实体
     * @return Result
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 30)
    Result<Void> save(@RequestBody RoomHistoryEntity roomHistory);

    /**
     * 更新
     *
     * @param roomHistory 实体
     * @return Result
     */
    @ApiOperation("更新")
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 40)
    Result<Void> update(@RequestBody RoomHistoryEntity roomHistory);

    /**
     * 根据ID数组批量删除
     *
     * @param ids ID数组
     * @return Result
     */
    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 50)
    Result<Void> delete(@RequestBody String[] ids);
}
