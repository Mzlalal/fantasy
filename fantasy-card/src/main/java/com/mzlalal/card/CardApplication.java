package com.mzlalal.card;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 * EnableFeignClients 开启feign调用
 * SpringBootApplication 扫描
 *
 * @author Mzlalal
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.mzlalal.base.feign"})
@SpringBootApplication(scanBasePackages = {"com.mzlalal.base", "com.mzlalal.card"}
        , exclude = {DataSourceAutoConfiguration.class})
public class CardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardApplication.class, args);
        System.out.println("The CardApplication Has Been Started Successfully!");
    }
}
