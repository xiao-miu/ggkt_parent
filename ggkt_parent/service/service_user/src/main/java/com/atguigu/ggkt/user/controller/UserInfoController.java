package com.atguigu.ggkt.user.controller;


import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-29
 */
//为了给service_activity   里获取 用户信息  查询己经获取使用优惠券列表做准备
@RestController
@RequestMapping("/admin/user/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "获取")
    @GetMapping("inner/getById/{id}")
    public UserInfo getById(@PathVariable Long id) {
        return userInfoService.getById(id);
    }
}

