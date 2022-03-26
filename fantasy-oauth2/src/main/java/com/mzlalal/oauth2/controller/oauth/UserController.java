package com.mzlalal.oauth2.controller.oauth;

import cn.hutool.core.collection.CollUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.entity.oss.req.UserVueSelectReq;
import com.mzlalal.base.feign.oauth2.UserFeignApi;
import com.mzlalal.base.util.Page;
import com.mzlalal.oauth2.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户controller
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 */
@Slf4j
@Api(tags = "用户")
@Validated
@RestController
@RequestMapping("/api/v1/oauth/user")
public class UserController implements UserFeignApi {
    /**
     * 用户操作
     */
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Result<UserEntity> list(@RequestBody Po<UserEntity> po) {
        return Result.ok(userService.queryPage(po));
    }

    @Override
    public Result<UserEntity> info(@PathVariable("id") Long id) {
        return Result.ok(userService.getById(id));
    }

    @Override
    public Result<Void> save(@Validated @RequestBody UserEntity user) {
        // 校验
        userService.verifyWhenSaveOrUpdate(user);
        // 设置默认普通角色
        user.setRoleId(GlobalConstant.DEFAULT_NORMAL_ROLE);
        return userService.save(user) ? Result.ok() : Result.fail();
    }

    @Override
    public Result<Void> update(@Validated @RequestBody UserEntity user) {
        // 校验
        userService.verifyWhenSaveOrUpdate(user);
        // 更新
        return userService.updateById(user) ? Result.ok() : Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        return userService.removeByIds(CollUtil.newArrayList(ids)) ? Result.ok() : Result.fail();
    }

    @Override
    public Result<VueSelect> queryUserIdVueSelectListByUsername(@Validated @RequestBody UserVueSelectReq req) {
        Page<VueSelect> page = Page.list(userService.queryUserIdVueSelectListByUsername(req.getUsername()));
        return Result.ok(page);
    }

    @Override
    public Result<VueSelect> queryUserMailVueSelectListByUsername(@Validated @RequestBody UserVueSelectReq req) {
        Page<VueSelect> page = Page.list(userService.queryUserMailVueSelectListByUsername(req.getUsername()));
        return Result.ok(page);
    }

}