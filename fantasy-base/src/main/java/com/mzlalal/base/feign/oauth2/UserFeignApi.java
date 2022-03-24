package com.mzlalal.base.feign.oauth2;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.entity.oss.req.UserVueSelectReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户信息feign调用类
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 **/
@FeignClient(contextId = "UserFeignApi", name = GlobalConstant.FANTASY_OAUTH2, url = "${fantasy-oauth2.feign.url}"
        , path = "/api/v1/oauth/user")
public interface UserFeignApi {

    /**
     * 查询分页信息
     *
     * @param po 分页参数
     * @return Result
     */
    @ApiOperation("查看列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 10)
    Result<UserEntity> list(@RequestBody Po<UserEntity> po);

    /**
     * 详情
     *
     * @param id ID 实体ID
     * @return Result
     */
    @ApiOperation("查看详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 20)
    Result<UserEntity> info(@PathVariable("id") Long id);

    /**
     * 保存
     *
     * @param user 实体
     * @return Result
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 30)
    Result<Void> save(@RequestBody UserEntity user);

    /**
     * 更新
     *
     * @param user 实体
     * @return Result
     */
    @ApiOperation("更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 40)
    Result<Void> update(@RequestBody UserEntity user);

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
     * 根据用户名查询用户ID下拉框集合
     *
     * @param req 请求
     * @return List<VueSelect>
     */
    @ApiOperation("根据用户名查询用户ID下拉框集合")
    @RequestMapping(value = "/query.user.id.vue.select.list.by.username", method = RequestMethod.POST)
    Result<VueSelect> queryUserIdVueSelectListByUsername(@RequestBody UserVueSelectReq req);

    /**
     * 根据用户名查询用户邮箱下拉框集合
     *
     * @param req 请求
     * @return List<VueSelect>
     */
    @ApiOperation("根据用户名查询用户邮箱下拉框集合")
    @RequestMapping(value = "/query.user.mail.vue.select.list.by.username", method = RequestMethod.POST)
    Result<VueSelect> queryUserMailVueSelectListByUsername(@RequestBody UserVueSelectReq req);
}
