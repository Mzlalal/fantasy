package com.mzlalal.oauth2.config.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.config.swagger.BaseFantasySwagger2Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
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
@Profile(GlobalConstant.DEV)
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2ConfigBase extends BaseFantasySwagger2Config {

    @Autowired
    public Swagger2ConfigBase(OpenApiExtensionResolver openApiExtensionResolver) {
        super(openApiExtensionResolver);
    }

    @Override
    public String moduleName() {
        return "oauth2";
    }

    @Override
    public String title() {
        return "用户及授权";
    }

    @Override
    public String description() {
        return "用户、客户端、授权(登录,验证码)";
    }

    @Override
    public String groupName() {
        return "V1";
    }
}
