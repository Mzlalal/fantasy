package com.mzlalal.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oauth2.ClientEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * dao
 *
 * @author Mzlalal
 * @date 2021-07-28 15:47:17
 */
@Mapper
public interface ClientDao extends BaseMapper<ClientEntity> {

}
