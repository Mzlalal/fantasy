package com.mzlalal.oauth2.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.entity.oauth2.req.UpdateUserByAdminReq;
import com.mzlalal.base.entity.oss.req.UserVueSelectReq;
import com.mzlalal.base.feign.oauth2.UserFeignApi;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oauth2.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    /**
     * redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserController(UserService userService, RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
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
        userService.verifyWhenSave(user);
        // 设置默认普通角色
        user.setRoleId(GlobalConstant.DEFAULT_NORMAL_ROLE);
        return userService.save(user) ? Result.ok() : Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody UserEntity user) {
        // 校验
        userService.verifyWhenUpdate(user);
        // 更新
        if (userService.updateById(user)) {
            // 更新用户信息并缓存
            UserEntity newUserEntity = userService.getById(user.getId());
            // 缓存设置新的用户信息
            redisTemplate.opsForValue().set(GlobalConstant.userToken(newUserEntity.getAccessToken()), newUserEntity);
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        return userService.removeByIds(CollUtil.newArrayList(ids)) ? Result.ok() : Result.fail();
    }

    @Override
    public Result<VueSelect> queryUserIdVueSelectListByUsername(@Validated @RequestBody UserVueSelectReq req) {
        FantasyPage<VueSelect> page = FantasyPage.list(userService.queryUserIdVueSelectListByUsername(req.getUsername()));
        return Result.ok(page);
    }

    @Override
    public Result<VueSelect> queryUserMailVueSelectListByUsername(@Validated @RequestBody UserVueSelectReq req) {
        FantasyPage<VueSelect> page = FantasyPage.list(userService.queryUserMailVueSelectListByUsername(req.getUsername()));
        return Result.ok(page);
    }

    @Override
    public Result<Void> updateByAdmin(@Validated @RequestBody UpdateUserByAdminReq req) {
        // 设置为空,不更新
        if (StrUtil.isBlank(req.getPassword())) {
            req.setPassword(null);
        }
        // 更新用户信息
        UserEntity user = new UserEntity();
        // bean拷贝
        BeanUtils.copyProperties(req, user);
        // 更新
        if (userService.updateById(user)) {
            return Result.ok();
        }
        return Result.fail();
    }

}
