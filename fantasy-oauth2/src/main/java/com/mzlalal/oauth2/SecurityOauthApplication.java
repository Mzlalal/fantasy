package com.mzlalal.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 * EnableFeignClients 开启feign调用
 * EnableDiscoveryClient nacos注册中心
 * SpringBootApplication 扫描
 * MapperScan mybatis扫描
 *
 * @author Mzlalal
 */
@MapperScan(basePackages = "com.mzlalal.oauth2.dao")
@EnableFeignClients(basePackages = {"com.mzlalal.base.feign"})
@SpringBootApplication(scanBasePackages = {"com.mzlalal.base", "com.mzlalal.oauth2"})
public class SecurityOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityOauthApplication.class, args);
    }

}
