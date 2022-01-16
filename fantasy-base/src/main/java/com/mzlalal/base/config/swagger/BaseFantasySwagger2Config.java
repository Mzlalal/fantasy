package com.mzlalal.base.config.swagger;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.mzlalal.base.common.GlobalResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        // 返回状态
        List<ResponseMessage> globalResponseList = new ArrayList<>();
        // 遍历枚举
        Arrays.stream(GlobalResult.values()).forEach(item -> globalResponseList
                .add(new ResponseMessageBuilder()
                        .code(item.getState())
                        .message(item.getMessage())
                        .responseModel(new ModelRef(item.getMessage())).build()
                ));
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, globalResponseList)
                .globalResponseMessage(RequestMethod.POST, globalResponseList)
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
