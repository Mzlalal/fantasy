package com.mzlalal.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageHelper;
import com.mzlalal.base.entity.global.po.PageInfo;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.util.Page;

/**
 * 基础Service
 *
 * @author Mzlalal
 * @date 2021/6/17 16:48
 **/
public interface BaseService<T> extends IService<T> {

    /**
     * 根据参数查询分页
     *
     * @param po 分页参数
     * @return 分页数据
     */
    Page<T> queryPage(Po<T> po);

    /**
     * 设置分页
     *
     * @param pageInfo 分页参数
     * @return com.github.pagehelper.Page<T>
     */
    default com.github.pagehelper.Page<T> createPageQuery(PageInfo pageInfo) {
        // 分页信息
        return PageHelper.startPage(pageInfo.getCurrPage(), pageInfo.getPageSize());
    }
}
