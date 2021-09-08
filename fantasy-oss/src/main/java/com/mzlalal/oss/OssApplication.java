package com.mzlalal.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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
@EnableFeignClients(basePackages = {"com.mzlalal.base.feign"})
@SpringBootApplication(scanBasePackages = {"com.mzlalal.base", "com.mzlalal.oss"}
        , exclude = {DataSourceAutoConfiguration.class})
public class OssApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class, args);
    }

}
