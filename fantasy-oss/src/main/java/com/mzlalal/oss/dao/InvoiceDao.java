package com.mzlalal.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oss.dto.InvoiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 发票dao
 *
 * @author Mzlalal
 * @date 2022-10-15 22:37:54
 */
@Mapper
public interface InvoiceDao extends BaseMapper<InvoiceEntity> {

    /**
     * 根据发票源地址集合查询发票
     *
     * @param sourceUrlSet 发票源地址集合
     * @return Set<InvoiceEntity>
     */
    Set<String> queryInvoiceBySourceUrl(@Param("sourceUrlSet") Set<String> sourceUrlSet);
}