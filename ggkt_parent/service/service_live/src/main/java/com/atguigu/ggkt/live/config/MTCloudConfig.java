package com.atguigu.ggkt.live.config;

import com.atguigu.ggkt.live.mtcloud.MTCloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Date:  2022/9/19
 */
@Component
public class MTCloudConfig{
    @Autowired
    private MTCloudAccountConfig  mTCloudAccountConfig;
    @Bean
    public MTCloud mtCloudClient(){
        return new MTCloud(mTCloudAccountConfig.getOpenId(), mTCloudAccountConfig.getOpenToken());
    }
}
