package com.mzlalal.base.entity.global;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * 基础entity
 *
 * @author Mzlalal
 * @date 2021/6/22 20:27
 **/
@ApiModel("基类")
public class BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 复制一个互不干扰的新的实体
     * (Clazz) clazz.copy()
     *
     * @return BaseEntity
     */
    public BaseEntity copy() {
        return ObjectUtil.clone(this);
    }
}
