package com.mzlalal.oss.controller;

import cn.hutool.core.collection.CollUtil;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DayMatterEntity;
import com.mzlalal.base.feign.oss.DayMatterFeignApi;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.base.util.LambdaUtil;
import com.mzlalal.oss.service.DayMatterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 纪念日controller
 *
 * @author Mzlalal
 * @date 2022-03-23 19:39:24
 */
@Api(tags = "纪念日")
@RestController
@RequestMapping("/api/v1/oss/day.matter")
public class DayMatterController implements DayMatterFeignApi {

    private final DayMatterService dayMatterService;

    @Autowired
    public DayMatterController(DayMatterService dayMatterService) {
        this.dayMatterService = dayMatterService;
    }

    @Override
    public Result<DayMatterEntity> list(@RequestBody Po<DayMatterEntity> po) {
        FantasyPage<DayMatterEntity> page = dayMatterService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<DayMatterEntity> info(@PathVariable("id") String id) {
        DayMatterEntity dayMatter = dayMatterService.getById(id);
        return Result.ok(dayMatter);
    }

    @Override
    public Result<Void> save(@Validated @RequestBody DayMatterEntity dayMatter) {
        // 提醒列表
        List<VueSelect> matterSelectSet = dayMatter.getMatterSelectSet();
        // 不能超过N人
        AssertUtil.isTrue(matterSelectSet.size() <= 10, "提醒列表最多保存10人");
        // 获取集合
        String collect = LambdaUtil.getFieldStr(matterSelectSet, (item) -> String.valueOf(item.getCode()), ",");
        // 根据","分隔,方便查询
        dayMatter.setMatterMailSet(collect);
        // 保存
        if (dayMatterService.save(dayMatter)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@Validated @RequestBody DayMatterEntity dayMatter) {
        // 提醒列表
        List<VueSelect> matterSelectSet = dayMatter.getMatterSelectSet();
        // 不能超过N人
        AssertUtil.isTrue(matterSelectSet.size() <= 10, "提醒列表最多保存10人");
        // 获取集合
        String collect = LambdaUtil.getFieldStr(matterSelectSet, (item) -> String.valueOf(item.getCode()), ",");
        // 根据","分隔,方便查询
        dayMatter.setMatterMailSet(collect);
        // 更新
        if (dayMatterService.updateById(dayMatter)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        if (dayMatterService.removeByIds(CollUtil.newArrayList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }

}
