package com.mzlalal.gateway.config.swagger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger资源配置类
 *
 * @author Mzlalal
 * @date 2022年1月13日 11:33:40
 **/
@Slf4j
@Primary
@Component
@AllArgsConstructor
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    /**
     * 路由
     */
    private final RouteLocator routeLocator;
    /**
     * 网关配置
     */
    private final GatewayProperties gatewayProperties;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resourceList = new ArrayList<>();
        List<String> routeList = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routeList.add(route.getId()));
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routeList.contains(routeDefinition.getId()))
                .forEach(route -> route.getPredicates().stream()
                        // 匹配存在Path的服务
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> resourceList.add(this.swaggerResource(
                                // 文档左上角下拉框的名字
                                route.getId(),
                                // swagger文档json信息的请求地址
                                predicateDefinition.getArgs()
                                        .get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                        .replace("**", "v2/api-docs?group=V1")))));
        return resourceList;
    }

    /**
     * 获取swagger resource资源
     *
     * @param name     文档左上角下拉框的名字
     * @param location 请求地址,分组需要增加group
     * @return SwaggerResource
     */
    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
