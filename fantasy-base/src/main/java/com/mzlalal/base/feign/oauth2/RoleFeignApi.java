package com.mzlalal.base.feign.oauth2;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.RoleEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 角色feign调用类
 *
 * @author Mzlalal
 * @date 2022-02-28 10:39:10
 **/
@FeignClient(contextId = "RoleFeignApi", name = GlobalConstant.FANTASY_OAUTH2, url = "${fantasy-oauth2.feign.url:}"
        , path = "/api/v1/oauth2/role")
public interface RoleFeignApi {

    /**
     * 查询分页信息
     *
     * @param po 分页参数
     * @return Result
     */
    @ApiOperation("查看列表")
    @RequestMapping(value = "/list" , method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 10)
    Result<RoleEntity> list(@RequestBody Po<RoleEntity> po);

    /**
     * 详情
     *
     * @param id ID 实体ID
     * @return Result
     */
    @ApiOperation("查看详情")
    @RequestMapping(value = "/info/{id}" , method = RequestMethod.GET)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 20)
    Result<RoleEntity> info(@PathVariable("id") Long id);

    /**
     * 保存
     *
     * @param role 实体
     * @return Result
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 30)
    Result<Void> save(@RequestBody RoleEntity role);

    /**
     * 更新
     *
     * @param role 实体
     * @return Result
     */
    @ApiOperation("更新")
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 40)
    Result<Void> update(@RequestBody RoleEntity role);

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
     * 查询角色下拉框集合
     *
     * @return Result<VueSelect>
     */
    @ApiOperation("查询角色下拉框集合")
    @RequestMapping(value = "/query.role.vue.select.list", method = RequestMethod.GET)
    Result<VueSelect> queryRoleVueSelectList();
}
