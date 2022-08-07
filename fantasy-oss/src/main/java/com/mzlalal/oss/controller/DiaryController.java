package com.mzlalal.oss.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DiaryEntity;
import com.mzlalal.base.feign.oss.DiaryFeignApi;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oss.service.DiaryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 飞虹controller
 *
 * @author Mzlalal
 * @date 2022-04-28 20:08:41
 */
@Api(tags = "飞虹")
@RestController
@RequestMapping("/api/v1/oss/diary")
public class DiaryController implements DiaryFeignApi {

    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @Override
    public Result<DiaryEntity> list(@RequestBody Po<DiaryEntity> po) {
        FantasyPage<DiaryEntity> page = diaryService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<DiaryEntity> info(@PathVariable("id") String id) {
        DiaryEntity diary = diaryService.getById(id);
        return Result.ok(diary);
    }

    @Override
    public Result<DiaryEntity> save(@Validated @RequestBody DiaryEntity diary) {
        if (diaryService.save(diary)) {
            return Result.ok(diary);
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@Validated @RequestBody DiaryEntity diary) {
        DiaryEntity entity = diaryService.getById(diary.getId());
        AssertUtil.notNull(entity, "动态查询不存在");
        AssertUtil.isTrue(StrUtil.equals(entity.getCreateBy(), Oauth2Context.getUserIdElseThrow())
                , "您无权修改当前动态");
        
        if (diaryService.updateById(diary)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        if (diaryService.removeByIds(CollUtil.newArrayList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Map<String, List<DiaryEntity>>> queryDiaryGroupByDate(@RequestBody Po<DiaryEntity> po) {
        return diaryService.queryDiaryGroupByDate(po);
    }
}
