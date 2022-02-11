package com.mzlalal.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 * EnableFeignClients 开启feign调用
 * SpringBootApplication 扫描
 * MapperScan mybatis扫描
 *
 * @author Mzlalal
 */
@EnableDiscoveryClient
@MapperScan(basePackages = "com.mzlalal.oauth2.dao")
@EnableFeignClients(basePackages = {"com.mzlalal.base.feign"})
@SpringBootApplication(scanBasePackages = {"com.mzlalal.base", "com.mzlalal.oauth2"})
public class SecurityOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityOauthApplication.class, args);
        System.out.println("The SecurityOauthApplication Has Been Started Successfully!");
    }
}
