package com.mzlalal.chess;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 * EnableFeignClients 开启feign调用
 * EnableTransactionManagement 事务管理
 * SpringBootApplication 扫描
 * MapperScan mybatis扫描
 *
 * @author Mzlalal
 */
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan(basePackages = "com.mzlalal.chess.dao")
@EnableFeignClients(basePackages = {"com.mzlalal.base.feign"})
@SpringBootApplication(scanBasePackages = {"com.mzlalal.base", "com.mzlalal.chess"})
public class ChessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChessApplication.class, args);
        System.out.println("The ChessApplication Has Been Started Successfully!");
    }
}
