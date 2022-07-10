package com.mzlalal.oauth2.controller;

import cn.hutool.core.collection.CollUtil;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.RoleEntity;
import com.mzlalal.base.feign.oauth2.RoleFeignApi;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oauth2.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色controller
 *
 * @author Mzlalal
 * @date 2022-02-28 10:39:10
 */
@Api(tags = "角色")
@RestController
@RequestMapping("/api/v1/oauth2/role")
public class RoleController implements RoleFeignApi {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Result<RoleEntity> list(@RequestBody Po<RoleEntity> po) {
        FantasyPage<RoleEntity> page = roleService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<RoleEntity> info(@PathVariable("id") Long id) {
        RoleEntity role = roleService.getById(id);
        return Result.ok(role);
    }

    @Override
    public Result<Void> save(@RequestBody RoleEntity role) {
        if (roleService.save(role)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody RoleEntity role) {
        if (roleService.updateById(role)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        if (roleService.removeByIds(CollUtil.newArrayList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<VueSelect> queryRoleVueSelectList() {
        // 查询列表
        List<VueSelect> vueSelectList = roleService.queryRoleVueSelectList();
        // 返回
        FantasyPage<VueSelect> page = FantasyPage.list(vueSelectList);
        return Result.ok(page);
    }

}
