package com.atguigu.ggkt.live.config;

import com.atguigu.ggkt.live.mtcloud.MTCloud;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Date:  2022/9/19
 */
@Data
@Component
//找到Properties里 mtcloud的值进行注入   和   value 效果一样
@ConfigurationProperties(prefix = "mtcloud")
public class MTCloudAccountConfig {
    private String openId;
    private String openToken;

//    @Bean
//    public MTCloud MTCloudAccountConfig(){
//        return new MTCloud(openId, openToken);
//    }
}
