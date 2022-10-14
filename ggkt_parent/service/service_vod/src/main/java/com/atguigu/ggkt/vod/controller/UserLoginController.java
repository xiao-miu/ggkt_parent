package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.utils.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Date:  2022/8/13
 */
//登录
//@CrossOrigin        //为了解决跨域问题
@RestController
@RequestMapping("/admin/vod/user/")
public class UserLoginController {

    @PostMapping("login")
//    {"code":20000,"data":{"token":"admin-token"}}
    public Result login() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }
    @GetMapping("info")
    public Result info() {
//        {"code":20000,"data":{"roles":["admin"],"introduction":"I am a super administrator",
//        "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}
        HashMap<String, Object> map = new HashMap<>();
        map.put("roles","admin");
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return Result.ok(map);
    }
}
