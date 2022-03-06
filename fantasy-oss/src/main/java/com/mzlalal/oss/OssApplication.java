package com.mzlalal.oss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 * EnableFeignClients 开启feign调用
 * SpringBootApplication 扫描
 *
 * @author Mzlalal
 */
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan(basePackages = "com.mzlalal.oss.dao")
@EnableFeignClients(basePackages = {"com.mzlalal.base.feign"})
@SpringBootApplication(scanBasePackages = {"com.mzlalal.base", "com.mzlalal.oss"})
public class OssApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class, args);
        System.out.println("The OssApplication Has Been Started Successfully!");
    }
}
