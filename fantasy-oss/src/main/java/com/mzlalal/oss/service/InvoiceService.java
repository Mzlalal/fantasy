package com.mzlalal.oss.service;

import com.mzlalal.base.entity.oss.dto.InvoiceEntity;
import com.mzlalal.base.entity.oss.req.BatchSaveInvoiceReq;
import com.mzlalal.base.service.BaseService;

import java.util.List;
import java.util.Set;

/**
 * 发票service
 *
 * @author Mzlalal
 * @date 2022-10-15 22:37:54
 */
public interface InvoiceService extends BaseService<InvoiceEntity> {

    /**
     * 根据文本分析发票URL地址
     *
     * @param text 文本
     * @return List<String> URL集合
     */
    List<String> analysisInvoiceUrlByText(String text);

    /**
     * 根据URL批量保存发票
     *
     * @param req 批量保存发票请求
     * @return List<BatchSaveInvoiceReq>
     */
    List<BatchSaveInvoiceReq> batchSaveInvoiceByUrl(List<BatchSaveInvoiceReq> req);

    /**
     * 根据发票源地址集合查询发票
     *
     * @param sourceUrlSet 发票源地址集合
     * @return Set<InvoiceEntity>
     */
    Set<String> queryInvoiceBySourceUrl(Set<String> sourceUrlSet);
}