package com.mzlalal.oauth2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oauth2.dto.RoleClientEntity;
import com.mzlalal.base.entity.oauth2.vo.ClientVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色与客户端的关联关系dao
 *
 * @author Mzlalal
 * @date 2022-02-28 22:48:43
 */
@Mapper
public interface RoleClientDao extends BaseMapper<RoleClientEntity> {

    /**
     * 根据角色ID查询客户端列表
     *
     * @param roleId 角色ID
     * @return List<ClientVo>
     */
    List<ClientVo> queryClientListByRoleId(@Param("roleId") String roleId);
}
