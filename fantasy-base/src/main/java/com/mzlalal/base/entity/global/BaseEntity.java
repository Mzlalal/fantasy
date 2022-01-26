package com.mzlalal.base.entity.global;

import cn.hutool.core.util.ObjectUtil;

import java.io.Serializable;

/**
 * 基础entity
 * 继承本类有序列化和克隆
 *
 * @author Mzlalal
 * @date 2021/6/22 20:27
 **/
public class BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1769948409042193972L;

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
