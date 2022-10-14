package com.atguigu.ggkt.order.api;

import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.order.service.impl.OrderInfoServiceImpl;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Date:  2022/9/12
 */
@RestController
@RequestMapping("order/orderInfo")
public class OrderInfoApiController {
    @Resource
    private OrderInfoService orderInfoService;
    @ApiOperation("新增点播课程订单")
    @PostMapping("submitOrder")
    public Result submitOrder(@RequestBody OrderFormVo orderFormVo, HttpServletRequest request){
        Long orderId = orderInfoService.subitOrder(orderFormVo);
        return Result.ok(orderId);
    }
    @ApiOperation(value = "根据订单id获取订单信息")
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id){
        OrderInfoVo orderInfoVoId = orderInfoService.getOrderInfoById(id);
        return Result.ok(orderInfoVoId);
    }
}
