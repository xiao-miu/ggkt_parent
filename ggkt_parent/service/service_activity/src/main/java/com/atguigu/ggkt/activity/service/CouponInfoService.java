package com.atguigu.ggkt.activity.service;

import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.activity.CouponUse;
import com.atguigu.ggkt.vo.activity.CouponUseQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-29
 */
public interface CouponInfoService extends IService<CouponInfo> {
    //获取已使用优惠券列表
    IPage<CouponUse> selectCouponUse(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo);
//    更新优惠券使用状态
    void updateCouponInfoUseStatus(Long couponUseId, Long orderId);
}
