package com.mzlalal.gateway.config.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.*;

import java.util.List;
import java.util.Optional;

/**
 * swagger资源设置,获取其他的文档地址然后合并
 *
 * @author Mzlalal
 * @date 2022年1月13日 11:33:40
 */
@RestController()
public class SwaggerHandler {

    /**
     * swagger SecurityConfiguration
     */
    private final SecurityConfiguration securityConfiguration;
    /**
     * swagger UiConfiguration
     */
    private final UiConfiguration uiConfiguration;
    /**
     * swagger资源提供方(来源于微服务)
     */
    private final SwaggerResourcesProvider swaggerResources;

    @Autowired
    public SwaggerHandler(SwaggerResourcesProvider swaggerResources
            , @Autowired(required = false) SecurityConfiguration securityConfiguration
            , @Autowired(required = false) UiConfiguration uiConfiguration) {
        this.swaggerResources = swaggerResources;
        this.securityConfiguration = securityConfiguration;
        this.uiConfiguration = uiConfiguration;
    }
    
    @GetMapping("/swagger-resources/configuration/security")
    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
        return Mono.just(new ResponseEntity<>(Optional.ofNullable(securityConfiguration)
                .orElse(SecurityConfigurationBuilder.builder().build()), HttpStatus.OK));
    }

    @GetMapping("/swagger-resources/configuration/ui")
    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
        return Mono.just(new ResponseEntity<>(Optional.ofNullable(uiConfiguration)
                .orElse(UiConfigurationBuilder.builder().build()), HttpStatus.OK));
    }

    @GetMapping("/swagger-resources")
    public Mono<ResponseEntity<List<SwaggerResource>>> swaggerResources() {
        return Mono.just((new ResponseEntity<>(swaggerResources.get(), HttpStatus.OK)));
    }
}

