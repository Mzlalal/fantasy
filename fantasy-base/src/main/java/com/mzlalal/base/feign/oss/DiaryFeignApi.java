package com.mzlalal.base.feign.oss;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DiaryEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * feign调用类
 *
 * @author Mzlalal
 * @date 2022-04-28 20:08:41
 **/
@FeignClient(contextId = "DiaryFeignApi", name = GlobalConstant.FANTASY_OSS, url = "${fantasy-oss.feign.url:}"
        , path = "/api/v1/oss/diary")
public interface DiaryFeignApi {

    /**
     * 查询分页信息
     *
     * @param po 分页参数
     * @return Result
     */
    @ApiOperation("查看列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 10)
    Result<DiaryEntity> list(@RequestBody Po<DiaryEntity> po);

    /**
     * 详情
     *
     * @param id ID 实体ID
     * @return Result
     */
    @ApiOperation("查看详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 20)
    Result<DiaryEntity> info(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param diary 实体
     * @return Result
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 30)
    Result<DiaryEntity> save(@Validated @RequestBody DiaryEntity diary);

    /**
     * 更新
     *
     * @param diary 实体
     * @return Result
     */
    @ApiOperation("更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 40)
    Result<Void> update(@Validated @RequestBody DiaryEntity diary);

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
     * 查询飞虹根据日期分组
     *
     * @param po 分页参数
     * @return Result<Map < Date, List < DiaryEntity>>>
     */
    @ApiOperation("查询飞虹根据日期分组")
    @RequestMapping(value = "/query.diary.group.by.date", method = RequestMethod.POST)
    Result<Map<String, List<DiaryEntity>>> queryDiaryGroupByDate(@RequestBody Po<DiaryEntity> po);
}