package com.mzlalal.base.feign.oss;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.InvoiceEntity;
import com.mzlalal.base.entity.oss.req.BatchSaveInvoiceReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * 发票feign调用类
 *
 * @author Mzlalal
 * @date 2022-10-15 22:37:54
 **/
@FeignClient(contextId = "InvoiceFeignApi", name = GlobalConstant.FANTASY_OSS, url = "${fantasy-oss.feign.url:}"
        , path = "/api/v1/oss/invoice")
public interface InvoiceFeignApi {

    /**
     * 查询分页信息
     *
     * @param po 分页参数
     * @return Result
     */
    @ApiOperation("查看列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 10)
    Result<InvoiceEntity> list(@RequestBody Po<InvoiceEntity> po);

    /**
     * 详情
     *
     * @param id ID 实体ID
     * @return Result
     */
    @ApiOperation("查看详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 20)
    Result<InvoiceEntity> info(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param invoice 实体
     * @return Result
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 30)
    Result<Void> save(@RequestBody InvoiceEntity invoice);

    /**
     * 更新
     *
     * @param invoice 实体
     * @return Result
     */
    @ApiOperation("更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperationSupport(order = Ordered.HIGHEST_PRECEDENCE + 40)
    Result<Void> update(@RequestBody InvoiceEntity invoice);

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
     * 根据文本分析发票URL地址
     *
     * @param text 文本
     * @return Result
     */
    @ApiOperation("根据文本分析发票URL地址")
    @RequestMapping(value = "/analysis.invoice.url.by.text", method = RequestMethod.POST)
    Result<String> analysisInvoiceUrlByText(@RequestBody String text);

    /**
     * 根据URL批量保存发票
     *
     * @param req 批量保存发票请求
     * @return Result
     */
    @ApiOperation("根据URL批量保存发票")
    @RequestMapping(value = "/batch.save.invoice.by.url", method = RequestMethod.POST)
    Result<BatchSaveInvoiceReq> batchSaveInvoiceByUrl(@Valid @RequestBody List<BatchSaveInvoiceReq> req);
}