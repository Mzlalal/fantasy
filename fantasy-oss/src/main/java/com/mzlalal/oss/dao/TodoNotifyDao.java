package com.mzlalal.oss.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 待办提醒dao
 *
 * @author Mzlalal
 * @date 2022-03-04 21:58:11
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface TodoNotifyDao extends BaseMapper<TodoNotifyEntity> {

}
