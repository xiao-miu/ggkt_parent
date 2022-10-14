package com.atguigu.ggkt.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Date:  2022/9/1
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.atguigu")
@MapperScan("com.atguigu.ggkt.wechat.mapper")   //mapper扫描
//@ComponentScan 的作用就是根据定义的扫描路径，把符合扫描规则的类装配到spring容器中//扫描Swagger2配置文件  测试接口 swagger-ui.html   地址
@ComponentScan(basePackages = "com.atguigu")
public class ServiceWechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceWechatApplication.class, args);
    }
}
