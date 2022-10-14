package com.atguigu.ggkt.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Date:  2022/8/27
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGetewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGetewayApplication.class);
    }
}
