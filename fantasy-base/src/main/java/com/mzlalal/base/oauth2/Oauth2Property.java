package com.mzlalal.base.oauth2;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

/**
 * oauth配置类
 *
 * @author Mzlalal
 * @date 2021/6/6 9:46
 **/
@Data
@Component
@NacosConfigurationProperties(dataId = "${spring.application.name}-oauth.properties")
@ConfigurationProperties(prefix = "mz.oauth")
public class Oauth2Property {

    @NotBlank(message = "authorize授权地址不能为空")
    private String authorize;

    @NotBlank(message = "获取token地址不能为空")
    private String accessToken;

    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    @NotBlank(message = "客户端私匙不能为空")
    private String clientSecret;

    @NotBlank(message = "登录地址不能为空")
    private String login;
}