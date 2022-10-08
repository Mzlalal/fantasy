package com.mzlalal.oss.controller;

import cn.hutool.core.collection.CollUtil;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.InvoiceEntity;
import com.mzlalal.base.entity.oss.req.BatchSaveInvoiceReq;
import com.mzlalal.base.feign.oss.InvoiceFeignApi;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oss.service.InvoiceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 发票controller
 *
 * @author Mzlalal
 * @date 2022-10-15 22:37:54
 */
@Api(tags = "发票管理")
@Validated
@RestController
@RequestMapping("/api/v1/oss/invoice")
public class InvoiceController implements InvoiceFeignApi {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public Result<InvoiceEntity> list(@RequestBody Po<InvoiceEntity> po) {
        FantasyPage<InvoiceEntity> page = invoiceService.queryPage(po);
        return Result.ok(page);
    }

    @Override
    public Result<InvoiceEntity> info(@PathVariable("id") String id) {
        InvoiceEntity invoice = invoiceService.getById(id);
        return Result.ok(invoice);
    }

    @Override
    public Result<Void> save(@RequestBody InvoiceEntity invoice) {
        if (invoiceService.save(invoice)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody InvoiceEntity invoice) {
        if (invoiceService.updateById(invoice)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        if (invoiceService.removeByIds(CollUtil.newArrayList(ids))) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<String> analysisInvoiceUrlByText(@RequestBody String text) {
        AssertUtil.notBlank(text, "文本不能为空");
        return Result.ok(FantasyPage.list(invoiceService.analysisInvoiceUrlByText(text)));
    }

    @Override
    public Result<BatchSaveInvoiceReq> batchSaveInvoiceByUrl(@Valid @RequestBody List<BatchSaveInvoiceReq> req) {
        return Result.ok(FantasyPage.list(invoiceService.batchSaveInvoiceByUrl(req)));
    }
}
