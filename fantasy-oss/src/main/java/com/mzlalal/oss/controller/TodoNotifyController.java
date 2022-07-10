package com.mzlalal.oss.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import com.mzlalal.base.feign.oss.TodoNotifyFeignApi;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oss.service.TodoNotifyService;
import com.mzlalal.oss.service.todo.NotifyTypeEnum;
import io.swagger.annotations.Api;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 待办提醒controller
 *
 * @author Mzlalal
 * @date 2022-03-04 21:58:11
 */
@Api(tags = "待办提醒")
@RestController
@RequestMapping("/api/v1/oss/todo.notify")
public class TodoNotifyController implements TodoNotifyFeignApi {

    private final TodoNotifyService todoNotifyService;

    @Autowired
    public TodoNotifyController(TodoNotifyService todoNotifyService) {
        this.todoNotifyService = todoNotifyService;
    }

    @Override
    public Result<TodoNotifyEntity> list(@RequestBody Po<TodoNotifyEntity> po) {
        FantasyPage<TodoNotifyEntity> page = todoNotifyService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<TodoNotifyEntity> info(@PathVariable("id") String id) {
        TodoNotifyEntity todoNotify = todoNotifyService.getById(id);
        return Result.ok(todoNotify);
    }

    @Override
    public Result<TodoNotifyEntity> save(@Validate @RequestBody TodoNotifyEntity todoNotify) {
        // 验证重复提醒的值
        NotifyTypeEnum.getEnum(todoNotify.getNotifyType()).checkAndCreateNextTime(todoNotify);
        // 设置邮箱
        todoNotify.setNotifyMailSet(Oauth2Context.getElseThrow().getMail());
        // 默认备注
        if (StrUtil.isBlank(todoNotify.getNotifyMemo())) {
            todoNotify.setNotifyMemo("无备注");
        }
        // 保存
        if (todoNotifyService.save(todoNotify)) {
            return Result.ok(todoNotify);
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody TodoNotifyEntity todoNotify) {
        // 获取下次执行时间
        NotifyTypeEnum.getEnum(todoNotify.getNotifyType()).checkAndCreateNextTime(todoNotify);
        // 设置邮箱
        todoNotify.setNotifyMailSet(Oauth2Context.getElseThrow().getMail());
        // 默认备注
        if (StrUtil.isBlank(todoNotify.getNotifyMemo())) {
            todoNotify.setNotifyMemo("无备注");
        }
        // 更新
        if (todoNotifyService.updateById(todoNotify)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        if (todoNotifyService.removeByIds(CollUtil.newArrayList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }

}
