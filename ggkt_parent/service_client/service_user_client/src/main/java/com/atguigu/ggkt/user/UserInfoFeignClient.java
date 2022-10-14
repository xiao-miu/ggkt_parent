package com.atguigu.ggkt.user;

import com.atguigu.ggkt.model.user.UserInfo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Date:  2022/8/30
 */
@FeignClient(value = "service-user")
public interface UserInfoFeignClient {
//    根据用户id得到用户信息
    @GetMapping("/admin/user/userInfo/inner/getById/{id}")
    UserInfo getById(@PathVariable Long id);


}
