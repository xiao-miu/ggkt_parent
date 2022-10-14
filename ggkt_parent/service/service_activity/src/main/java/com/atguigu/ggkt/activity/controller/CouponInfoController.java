package com.atguigu.ggkt.activity.controller;


import com.atguigu.ggkt.activity.service.CouponInfoService;
import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.activity.CouponUse;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vo.activity.CouponUseQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-29
 */
@Api(tags = "获取优惠券")
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {
    @Resource
    private CouponInfoService couponInfoService;
    @ApiOperation(value = "获取优惠券")
    @GetMapping("get/{id}")
    public Result get(@PathVariable String id) {
        CouponInfo couponInfo = couponInfoService.getById(id);
        return Result.ok(couponInfo);
    }

    @ApiOperation(value = "新增优惠券")
    @PostMapping("save")
    public Result save(@RequestBody CouponInfo couponInfo) {
        couponInfoService.save(couponInfo);
        return Result.ok();
    }

    @ApiOperation(value = "修改优惠券")
    @PutMapping("update")
    public Result updateById(@RequestBody CouponInfo couponInfo) {
        couponInfoService.updateById(couponInfo);
        return Result.ok();
    }

    @ApiOperation(value = "删除优惠券")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        couponInfoService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value="根据id列表删除优惠券")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<String> idList){
        couponInfoService.removeByIds(idList);
        return Result.ok();
    }
    //优惠券分页列表方法
    //page当前页   limit每页记录数
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,@PathVariable Long limit){
        Page<CouponInfo> pageParam = new Page<>(page,limit);
        Page<CouponInfo> couponInfoPage = couponInfoService.page(pageParam);
        return Result.ok(couponInfoPage);
    }
    //获取己经使用优惠券列表（条件查询分页)
    @PostMapping("couponUse/{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit, CouponUseQueryVo couponUseQueryVo){
        Page<CouponUse> pageParam = new Page<>(page,limit);
        IPage<CouponUse> couponUsePage = couponInfoService.selectCouponUse(pageParam, couponUseQueryVo);
        return Result.ok(couponUsePage);
    }
}

