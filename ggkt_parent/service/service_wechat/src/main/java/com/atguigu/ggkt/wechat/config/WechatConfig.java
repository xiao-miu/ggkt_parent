package com.atguigu.ggkt.wechat.config;

import com.atguigu.ggkt.wechat.utils.ConstantPropertiesUtil;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Date:  2022/9/2
 * **weixin-java-mp**是封装好了的微信接口客户端，使用起来很方便，后续我们就使用weixin-java-mp处理微信平台接口。
 */
@Component
public class WechatConfig {
    @Resource
    private ConstantPropertiesUtil constantPropertiesUtil;

    @Bean
    public WxMpService wxMpService(){
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    private WxMpConfigStorage wxMpConfigStorage() {
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(ConstantPropertiesUtil.ACCESS_KEY_ID);
        wxMpConfigStorage.setSecret(ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        return wxMpConfigStorage;
    }

}
