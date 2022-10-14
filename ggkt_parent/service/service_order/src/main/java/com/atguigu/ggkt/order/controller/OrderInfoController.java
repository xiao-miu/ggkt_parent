package com.atguigu.ggkt.order.controller;


import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-28
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/admin/order/orderInfo")
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result listOrder(@PathVariable Long page , @PathVariable Long limit , OrderInfoQueryVo orderInfoQueryVo) {
        //创建分页对象
        Page<OrderInfo> pages = new Page<>(page,limit);
        Map<String, Object> pageMap = orderInfoService.selectOrderInfoPage(pages,orderInfoQueryVo);
        return Result.ok(pageMap);
    }
}

