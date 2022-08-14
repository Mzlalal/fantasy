package com.mzlalal.gateway.config.swagger;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
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

    /**
     * 外部微服务swagger可能存在多个group的情况
     * 有解决思路为能够用gateway配置去处理,暂不去实现
     *
     * @return List<SwaggerResource> swagger资源配置
     */
    @Override
    public List<SwaggerResource> get() {
        // 路由ID集合
        List<String> routeList = new ArrayList<>();
        // swagger资源配置
        List<SwaggerResource> swaggerResourceList = new ArrayList<>();
        // 不知道这是干嘛的
        routeLocator.getRoutes().subscribe(route -> routeList.add(route.getId()));
        // 遍历gateway配置通过路由定义处理增加swagger资源配置
        gatewayProperties.getRoutes().stream()
                .filter(route -> routeList.contains(route.getId()))
                .forEach(route -> this.processPredicate(route, swaggerResourceList));
        return swaggerResourceList;
    }

    /**
     * 通过路由定义处理增加swagger资源配置
     *
     * @param route        gateway配置 spring.cloud.gateway.routes
     * @param resourceList swagger资源配置
     */
    private void processPredicate(RouteDefinition route, List<SwaggerResource> resourceList) {
        route.getPredicates().stream()
                // 匹配存在Path的服务
                .filter(predicate -> ("Path").equalsIgnoreCase(predicate.getName()))
                .forEach(predicate ->
                        resourceList.add(this.createSwaggerResource(route.getId(), predicate)));
    }

    /**
     * 生成swagger资源配置
     *
     * @param name      文档左上角下拉框的名字
     * @param predicate predicate路径定义
     * @return SwaggerResource
     */
    private SwaggerResource createSwaggerResource(String name, PredicateDefinition predicate) {
        SwaggerResource swaggerResource = new SwaggerResource();
        // 获取路由URL
        String url = predicate.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0");
        // 分组名
        CharSequence group = UrlBuilder.ofHttp(url).getQuery().get("group");
        // 如果下拉框的名字不包含分组名
        if (!StrUtil.contains(name, group)) {
            name = group + "-" + name;
        }
        // 文档左上角下拉框的名字
        swaggerResource.setName(name);
        // 点击下拉框后请求的地址
        swaggerResource.setLocation(url.replace("**", "v2/api-docs"));
        // swagger版本
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
