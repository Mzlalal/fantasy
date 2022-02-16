package com.mzlalal.oauth2.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oauth2.dto.ClientEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * dao
 *
 * @author Mzlalal
 * @date 2021-07-28 15:47:17
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ClientDao extends BaseMapper<ClientEntity> {

}