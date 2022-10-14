package com.atguigu.ggkt.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Date:  2022/8/28
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu")   //扫描Swagger2配置文件  测试接口 swagger-ui.html   地址
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.atguigu")
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class);
    }
}
