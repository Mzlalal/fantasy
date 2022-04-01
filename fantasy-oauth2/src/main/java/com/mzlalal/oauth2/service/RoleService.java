package com.mzlalal.oauth2.service;

import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.oauth2.dto.RoleEntity;
import com.mzlalal.base.service.BaseService;

import java.util.List;

/**
 * 角色与客户端的关联关系service
 *
 * @author Mzlalal
 * @date 2022-02-28 10:39:10
 */
public interface RoleService extends BaseService<RoleEntity> {

    /**
     * 查询角色下拉框集合
     *
     * @return List<VueSelect>
     */
    List<VueSelect> queryRoleVueSelectList();
}
