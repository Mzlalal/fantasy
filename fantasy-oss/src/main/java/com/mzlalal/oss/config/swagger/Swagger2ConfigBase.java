package com.mzlalal.oss.config.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.mzlalal.base.config.swagger.BaseFantasySwagger2Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * swagger配置类
 *
 * @author Mzlalal
 * @date 2021年6月20日 23:12:08
 **/
@Component
@Configuration
@EnableKnife4j
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
@ConditionalOnProperty(value = "knife.enable", matchIfMissing = true)
public class Swagger2ConfigBase extends BaseFantasySwagger2Config {

    @Autowired
    public Swagger2ConfigBase(OpenApiExtensionResolver openApiExtensionResolver) {
        super(openApiExtensionResolver);
    }

    @Override
    public String moduleName() {
        return "oss";
    }

    @Override
    public String title() {
        return "对象存储服务";
    }

    @Override
    public String description() {
        return "对象存储服务";
    }

    @Override
    public String groupName() {
        return "V1";
    }
}
