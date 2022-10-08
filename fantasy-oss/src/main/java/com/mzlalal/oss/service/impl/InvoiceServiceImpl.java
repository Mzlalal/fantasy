package com.mzlalal.oss.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.InvoiceEntity;
import com.mzlalal.base.entity.oss.req.BatchSaveInvoiceReq;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.minio.service.MinioService;
import com.mzlalal.oss.dao.InvoiceDao;
import com.mzlalal.oss.service.InvoiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 发票ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-10-15 22:37:54
 */
@Slf4j
@AllArgsConstructor
@Service("invoiceServiceImpl")
public class InvoiceServiceImpl extends ServiceImpl<InvoiceDao, InvoiceEntity> implements InvoiceService {
    /**
     * minIo服务
     */
    private final MinioService minioService;

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public FantasyPage<InvoiceEntity> queryPage(Po<InvoiceEntity> po) {
        // 查询参数
        QueryWrapper<InvoiceEntity> wrapper = new QueryWrapper<>(po.getEntity());
        // 创建分页条件
        Page<InvoiceEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<InvoiceEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    public List<String> analysisInvoiceUrlByText(String text) {
        return ReUtil.findAllGroup0(GlobalConstant.URL_REG_EXP, text);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BatchSaveInvoiceReq> batchSaveInvoiceByUrl(List<BatchSaveInvoiceReq> req) {
        // 当前时间
        Date now = new Date();
        // 保存列表
        List<InvoiceEntity> batchInvoiceList = new ArrayList<>();
        // 重试列表
        List<BatchSaveInvoiceReq> retryList = new ArrayList<>();

        // 根据源地址查询已经存在的地址
        Set<String> collect = req.stream().map(BatchSaveInvoiceReq::getInvoiceSourceUrl).collect(Collectors.toSet());
        Set<String> sourceUrlSet = this.getBaseMapper().queryInvoiceBySourceUrl(collect);

        // 遍历URL上传到minIo
        for (BatchSaveInvoiceReq item : req) {
            // 判断URL是否已经上传过
            if (sourceUrlSet.contains(item.getInvoiceSourceUrl())) {
                // 返回提示信息
                item.setTip("上传文件失败:发票源地址已存在");
                // 添加到重试列表
                retryList.add(item);
                continue;
            }
            // 默认名称
            String defaultName = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATETIME_MS_PATTERN) + item.getInvoiceExtName();
            // 发票名称
            String invoiceName = StrUtil.blankToDefault(item.getInvoiceName(), defaultName);

            // 下载文件
            byte[] downloadBytes = HttpUtil.downloadBytes(item.getInvoiceSourceUrl());

            // 上传到minIo
            String minIoPath;
            try {
                minIoPath = minioService.upload(invoiceName, new BufferedInputStream(new ByteArrayInputStream(downloadBytes)));
            } catch (Exception e) {
                log.error("根据URL批量保存发票上传minIo失败,失败参数:{}", item, e);
                // 返回提示信息
                item.setTip(StrUtil.format("上传文件失败:{}", e.getMessage()));
                // 添加到重试列表
                retryList.add(item);
                continue;
            }

            // 添加到保存列表
            batchInvoiceList.add(InvoiceEntity.builder()
                    .invoiceName(invoiceName)
                    .invoiceUrl(minIoPath)
                    .invoiceSourceUrl(item.getInvoiceSourceUrl())
                    .invoiceDate(now)
                    .build());
        }
        // 批量保存
        this.saveBatch(batchInvoiceList);
        // 返回重试列表
        return retryList;
    }

    @Override
    public Set<String> queryInvoiceBySourceUrl(Set<String> sourceUrlSet) {
        return this.getBaseMapper().queryInvoiceBySourceUrl(sourceUrlSet);
    }
}