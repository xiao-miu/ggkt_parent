package com.atguigu.ggkt.order.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Date:  2022/8/28
 */
@Configuration
@MapperScan("com.atguigu.ggkt.order.mapper")//@MapperScan 是扫描mapper类的注解,就不用在每个dao层的各个类上加@Mapper了
public class OrderConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
