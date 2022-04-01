package com.mzlalal.oauth2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.oauth2.dto.RoleEntity;
import com.mzlalal.base.entity.oauth2.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 角色dao
 *
 * @author Mzlalal
 * @date 2022-02-28 10:39:10
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {

    /**
     * 根据角色ID查询记录
     *
     * @param id 角色ID
     * @return List<RoleVo>
     */
    List<RoleVo> queryRoleListById(@PathParam("id") String id);

    /**
     * 查询角色下拉框集合
     *
     * @return List<VueSelect>
     */
    List<VueSelect> queryRoleVueSelectList();

}
