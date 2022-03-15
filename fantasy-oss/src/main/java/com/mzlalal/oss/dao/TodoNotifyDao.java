package com.mzlalal.oss.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oss.dto.TodoNotifyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 待办提醒dao
 *
 * @author Mzlalal
 * @date 2022-03-04 21:58:11
 */
@Mapper
public interface TodoNotifyDao extends BaseMapper<TodoNotifyEntity> {

    /**
     * 根据当前时间查询代表列表
     *
     * @param currentTime 当前时间
     * @return List<TodoNotifyEntity>
     */
    @InterceptorIgnore(tenantLine = "true")
    List<TodoNotifyEntity> queryTodoListByCurrentTime(@Param("currentTime") String currentTime);

}
