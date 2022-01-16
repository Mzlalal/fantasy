package com.mzlalal.base.util;

import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.po.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 * @author Mzlalal
 * @date 2021年4月25日 22:23:59
 */
@Data
@ApiModel("分页数据")
@EqualsAndHashCode(callSuper = true)
public class Page<T> extends BaseEntity {

    @ApiModelProperty("总行数")
    private long totalCount;

    @ApiModelProperty("每页记录数")
    private long pageSize;

    @ApiModelProperty("总页数")
    private long totalPage;

    @ApiModelProperty("当前页数")
    private long currPage;

    @ApiModelProperty("列表数据")
    private List<T> list;

    /**
     * 分页结果
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageInfo   分页参数
     */
    public Page(List<T> list, long totalCount, PageInfo pageInfo) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageInfo.getPageSize();
        this.currPage = pageInfo.getCurrPage();
        this.totalPage = (long) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 空分页信息
     *
     * @param pageInfo 分页参数
     * @return 空分页信息
     */
    public static <T> Page<T> empty(PageInfo pageInfo) {
        return new Page<>(new ArrayList<>(), 0L, pageInfo);
    }

    /**
     * 空分页信息
     *
     * @return 空分页信息
     */
    public static <T> Page<T> empty() {
        return new Page<>(new ArrayList<>(), 0L, new PageInfo());
    }
}
