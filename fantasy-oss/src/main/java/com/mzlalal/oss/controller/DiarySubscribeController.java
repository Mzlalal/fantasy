package com.mzlalal.oss.controller;

import cn.hutool.core.collection.CollUtil;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DiarySubscribeEntity;
import com.mzlalal.base.feign.oss.DiarySubscribeFeignApi;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.Page;
import com.mzlalal.oss.enums.SubscribeStatusEnum;
import com.mzlalal.oss.service.DiarySubscribeService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 尺墨飞虹订阅表controller
 *
 * @author Mzlalal
 * @date 2022-05-29 21:29:53
 */
@Api(tags = "飞虹订阅")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/oss/diary.subscribe")
public class DiarySubscribeController implements DiarySubscribeFeignApi {
    /**
     * 订阅service
     */
    private final DiarySubscribeService diarySubscribeService;

    @Override
    public Result<DiarySubscribeEntity> list(@RequestBody Po<DiarySubscribeEntity> po) {
        Page<DiarySubscribeEntity> page = diarySubscribeService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<DiarySubscribeEntity> info(@PathVariable("id") String id) {
        DiarySubscribeEntity diarySubscribe = diarySubscribeService.getById(id);
        return Result.ok(diarySubscribe);
    }

    @Override
    public Result<DiarySubscribeEntity> save(@RequestBody DiarySubscribeEntity diarySubscribe) {
        if (diarySubscribeService.save(diarySubscribe)) {
            return Result.ok(diarySubscribe);
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody DiarySubscribeEntity diarySubscribe) {
        if (diarySubscribeService.updateById(diarySubscribe)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        if (diarySubscribeService.removeByIds(CollUtil.newArrayList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> batchSave(@RequestBody String[] ids) {
        // 根据ID数组遍历生成订阅请求
        List<DiarySubscribeEntity> diarySubscribeEntityList = new ArrayList<>();
        for (String id : ids) {
            DiarySubscribeEntity diarySubscribeEntity = new DiarySubscribeEntity();
            diarySubscribeEntity.setSubscribeUserId(id);
            diarySubscribeEntity.setSubscribeStatus(SubscribeStatusEnum.SUBSCRIBE_CONFIRM.getStatus());
            diarySubscribeEntityList.add(diarySubscribeEntity);
        }
        // 批量保存
        if (diarySubscribeService.saveBatch(diarySubscribeEntityList)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> saveOrUpdate(DiarySubscribeEntity diarySubscribe) {
        if (diarySubscribeService.saveOrUpdate(diarySubscribe)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<DiarySubscribeEntity> followerList(@RequestBody Po<DiarySubscribeEntity> po) {
        return Result.ok(diarySubscribeService.followerList(po));
    }

    @Override
    public Result<DiarySubscribeEntity> subscribeList(@RequestBody Po<DiarySubscribeEntity> po) {
        return Result.ok(diarySubscribeService.subscribeList(po));
    }

    @Override
    public Result<DiarySubscribeEntity> applySubscribeList(@RequestBody Po<String> po) {
        AssertUtil.notBlank(po.getEntity(), "关键字不能为空");
        return Result.ok(diarySubscribeService.applySubscribeList(po));
    }

    @Override
    public Result<Void> notifyFollower() {
        // 通知我的粉丝
        diarySubscribeService.notifyFollower();
        return Result.ok();
    }
}
