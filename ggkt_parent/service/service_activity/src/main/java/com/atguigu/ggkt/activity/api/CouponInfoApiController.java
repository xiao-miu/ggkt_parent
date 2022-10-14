package com.atguigu.ggkt.activity.api;

import com.atguigu.ggkt.activity.controller.CouponInfoController;
import com.atguigu.ggkt.activity.service.CouponInfoService;
import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Date:  2022/9/12
 */
@Api(tags = "优惠券接口")
@RestController
@RequestMapping("/api/activity/couponInfo")
public class CouponInfoApiController {
    @Resource
    private CouponInfoService couponInfoService;

    //根据id查询优惠券信息
    @ApiOperation(value = "获取优惠券")
    @GetMapping("inner/getById/{couponId}")
    public CouponInfo getById(@PathVariable Long couponId){
        CouponInfo byId = couponInfoService.getById(couponId);
        return byId;
    }
    @ApiOperation(value = "更新优惠券使用状态")
    @GetMapping(value = "inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId, @PathVariable("orderId") Long orderId) {
        couponInfoService.updateCouponInfoUseStatus(couponUseId, orderId);
        return true;
    }
}
