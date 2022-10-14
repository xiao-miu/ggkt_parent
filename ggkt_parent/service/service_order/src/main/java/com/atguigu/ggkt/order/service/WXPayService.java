package com.atguigu.ggkt.order.service;

import com.atguigu.ggkt.order.service.impl.WXPayServiceImpl;

import java.util.Map;

/**
 * Date:  2022/9/14
 * 支付接口
 */
public interface WXPayService {
    //微信支付
    Map<String, Object> createJsapi(String orderNo);
//    根据订单号调用微信接口查询支付状态
    Map<String, String> queryPayStatus(String orderNo);
}
