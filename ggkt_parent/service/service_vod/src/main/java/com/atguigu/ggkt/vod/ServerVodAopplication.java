package com.atguigu.ggkt.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Date:  2022/8/11
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu")   //扫描Swagger2配置文件  测试接口 swagger-ui.html   地址
//@EnableTransactionManagement
@EnableDiscoveryClient
public class ServerVodAopplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerVodAopplication.class, args);
    }
}
