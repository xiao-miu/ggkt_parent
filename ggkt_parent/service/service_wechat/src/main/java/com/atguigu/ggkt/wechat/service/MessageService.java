package com.atguigu.ggkt.wechat.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Date:  2022/9/4
 * 微信公众号消息功能实现
 */
public interface MessageService {
    //接收微信服务器发送来的消息
    String receiveMessage(Map<String, String> param);

//    模板消息
    void pushPayMessage(long orderId);

}
