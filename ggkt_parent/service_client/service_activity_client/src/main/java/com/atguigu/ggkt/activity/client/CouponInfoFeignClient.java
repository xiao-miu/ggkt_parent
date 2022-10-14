package com.atguigu.ggkt.activity.client;

import com.atguigu.ggkt.model.activity.CouponInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Date:  2022/9/13
 */
@FeignClient(value = "service-activity")
public interface CouponInfoFeignClient {
    //    获取优惠券
    @GetMapping("/api/activity/couponInfo/inner/getById/{couponId}")
    CouponInfo getById(@PathVariable Long couponId);
    //    更新优惠券使用状态
    @GetMapping(value = "/api/activity/couponInfo/inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId, @PathVariable("orderId") Long orderId);
}

