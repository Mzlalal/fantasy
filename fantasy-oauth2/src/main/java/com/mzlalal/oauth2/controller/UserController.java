package com.mzlalal.oauth2.controller;

import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.UserEntity;
import com.mzlalal.base.feign.oauth2.UserFeignApi;
import com.mzlalal.oauth2.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 用户controller
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 */
@Slf4j
@Api(tags = "用户")
@RestController
@RequestMapping("/api/v1/oauth/user")
public class UserController implements UserFeignApi {

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
    public Result<Void> save(@RequestBody UserEntity user) {
        return userService.save(user) ? Result.ok() : Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody UserEntity user) {
        return userService.updateById(user) ? Result.ok() : Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody Long[] ids) {
        return userService.removeByIds(Collections.singletonList(ids)) ? Result.ok() : Result.fail();
    }

}
