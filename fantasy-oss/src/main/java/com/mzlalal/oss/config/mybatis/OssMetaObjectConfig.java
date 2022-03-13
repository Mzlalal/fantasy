package com.mzlalal.oss.config.mybatis;

import cn.hutool.core.map.MapUtil;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.mybatis.interceptor.mybatis.BaseMetaObjectInterceptor;
import com.mzlalal.mybatis.interceptor.prop.BaseMetaInterceptorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * mybatis 基础参数配置信息
 *
 * @author Mzlalal
 * @date 2022/3/13 23:24
 */
@Component
@ConditionalOnProperty(value = {"mz.mybatis.config.enable-base-meta"}, havingValue = "true")
public class OssMetaObjectConfig extends BaseMetaObjectInterceptor {
    public OssMetaObjectConfig(BaseMetaInterceptorProperties baseMetaInterceptorProperties) {
        super(baseMetaInterceptorProperties);
    }

    @Override
    protected Map<String, Object> processBaseField() {
        // 获取当前日期
        Date now = new Date();
        // 租户ID
        String tenantId = Oauth2Context.getTenantIdElseDefault();
        // 用户ID
        String userId = Oauth2Context.getUserIdElseDefault();
        // 用户名称
        String userName = Oauth2Context.getUsernameElseDefault();
        // 基础参数列表
        return MapUtil.<String, Object>builder()
                .put("createBy", userId)
                .put("updateBy", userId)
                .put("createName", userName)
                .put("updateName", userName)
                .put("createTime", now)
                .put("updateTime", now)
                .put("tenantId", tenantId)
                .build();
    }
}
