package com.mzlalal.base.config.swagger;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger配置类
 *
 * @author Mzlalal
 * @date 2021年6月20日 23:12:16
 **/
public abstract class BaseFantasySwagger2Config {
    /**
     * 引入Knife4j提供的扩展类
     */
    private final OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * 设置模块名称
     *
     * @return 模块名称
     */
    public abstract String moduleName();

    /**
     * 设置标题
     *
     * @return 标题
     */
    public abstract String title();

    /**
     * 设置描述
     *
     * @return 描述
     */
    public abstract String description();

    /**
     * 设置分组
     *
     * @return 分组
     */
    public abstract String groupName();

    @Autowired
    public BaseFantasySwagger2Config(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean("defaultApiV1")
    public Docket defaultApiV1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .groupName(this.groupName())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mzlalal." + this.moduleName() + ".controller"))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(this.groupName()));
    }

    /**
     * 接口文档说明
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(this.title())
                .description(this.description())
                .contact(new Contact("Mzlalal", "#", "mzlalal@163.com"))
                .version(this.groupName())
                .build();
    }
}
