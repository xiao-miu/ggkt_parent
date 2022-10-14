package com.atguigu.ggkt.order.service;

import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.atguigu.ggkt.vo.order.OrderInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-28
 */
public interface OrderInfoService extends IService<OrderInfo> {
    //创建分页对象
    Map<String, Object> selectOrderInfoPage(Page<OrderInfo> pages, OrderInfoQueryVo orderInfoQueryVo);
//    新增点播课程订单
    Long subitOrder(OrderFormVo orderFormVo);
//    根据订单id获取订单信息
    OrderInfoVo getOrderInfoById(Long id);
    //    更新订单支付状态:已支付成功
    void updateOrderStatus(String out_trade_no);
}

