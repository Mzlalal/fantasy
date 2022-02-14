package com.mzlalal.oauth2.config.bean;

import cn.hutool.core.map.MapUtil;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.mybatis.interceptor.mybatis.BaseMetaObjectInterceptor;
import com.mzlalal.mybatis.interceptor.prop.MyBatisPlusProperties;
import com.mzlalal.mybatis.interceptor.util.TenantIdService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * mybatis 基础参数配置信息
 *
 * @author Mzlalal
 * @date 2022/2/14 14:24
 */
@Component
public class Oauth2MetaObjectConfig extends BaseMetaObjectInterceptor {
    public Oauth2MetaObjectConfig(MyBatisPlusProperties myBatisPlusProperties, TenantIdService tenantIdService) {
        super(myBatisPlusProperties, tenantIdService);
    }

    @Override
    protected Map<String, Object> processBaseField() {
        // 获取当前日期
        Date now = new Date();
        // 租户ID
        String tenantId = Oauth2Context.getTenantId();
        // 基础参数列表
        return MapUtil.<String, Object>builder()
                .put("createTime" , now)
                .put("updateTime" , now)
                .put("tenantId" , tenantId)
                .build();
    }
}
