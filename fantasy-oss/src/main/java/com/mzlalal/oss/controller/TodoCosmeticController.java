package com.mzlalal.oss.controller;

import cn.hutool.core.collection.CollUtil;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.TodoCosmeticEntity;
import com.mzlalal.base.feign.oss.TodoCosmeticFeignApi;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oss.enums.NotifyTypeEnum;
import com.mzlalal.oss.service.TodoCosmeticService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 化妆品待办controller
 *
 * @author Mzlalal
 * @date 2026-01-03 13:16:16
 */
@Api(tags = "化妆品待办")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/oss/todo.cosmetic")
public class TodoCosmeticController implements TodoCosmeticFeignApi {

    /**
     * 化妆品待办Service
     */
    private final TodoCosmeticService todoCosmeticService;

    @Override
    public Result<TodoCosmeticEntity> list(@RequestBody Po<TodoCosmeticEntity> po) {
        FantasyPage<TodoCosmeticEntity> page = todoCosmeticService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<TodoCosmeticEntity> info(@PathVariable("id") String id) {
        TodoCosmeticEntity todoCosmetic = todoCosmeticService.getById(id);
        return Result.ok(todoCosmetic);
    }

    @Override
    public Result<TodoCosmeticEntity> save(@Validated @RequestBody TodoCosmeticEntity todoCosmetic) {
        // 验证重复提醒的值
        NotifyTypeEnum.getEnum(todoCosmetic.getNotifyType()).checkAndCreateNextTime(todoCosmetic);
        // 设置邮箱
        todoCosmetic.setNotifyMailSet(Oauth2Context.getElseThrow().getMail());
        // 保存
        if (todoCosmeticService.save(todoCosmetic)) {
            return Result.ok(todoCosmetic);
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody TodoCosmeticEntity todoCosmetic) {
        // 获取下次执行时间
        NotifyTypeEnum.getEnum(todoCosmetic.getNotifyType()).checkAndCreateNextTime(todoCosmetic);
        // 设置邮箱
        todoCosmetic.setNotifyMailSet(Oauth2Context.getElseThrow().getMail());
        // 更新
        if (todoCosmeticService.updateById(todoCosmetic)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        if (todoCosmeticService.removeByIds(CollUtil.newArrayList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }
}
