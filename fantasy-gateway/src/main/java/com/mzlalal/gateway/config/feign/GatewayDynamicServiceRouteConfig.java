package com.mzlalal.gateway.config.feign;

import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.environment.FeignEnvProcessor;
import com.mzlalal.base.util.AssertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关动态配置访问注册中心服务或本地服务
 *
 * @author Mzlalal
 * @date 2022/1/14 23:22
 */
@Configuration
public class GatewayDynamicServiceRouteConfig {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public RouteLocator dynamicServiceRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        // 获取总路由配置
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        // 生产环境就不设置动态路由
        if (StrUtil.equals(GlobalConstant.PRODUCT, activeProfile)) {
            return routes.build();
        }
        // 获取需要动态设置路由的服务
        for (String serviceId : FeignEnvProcessor.getLocalServiceStartedIdList()) {
            // 获取feign访问端口
            String servicePort = FeignEnvProcessor.getServicePort(serviceId);
            AssertUtil.notBlank(servicePort, "serviceId:{}未设置feign访问端口(请检查feign-service.properties)"
                    , serviceId);
            // 本地服务启动时直接访问本地服务,本地服务未启动访问注册中心的服务
            routes.route(serviceId, route -> route
                    .path(StrUtil.format("/{}/**", serviceId))
                    .filters(gatewayFilterSpec -> gatewayFilterSpec.stripPrefix(1))
                    .uri(StrUtil.format("http://localhost:{}", servicePort)))
                    .build();
        }
        return routes.build();
    }
}