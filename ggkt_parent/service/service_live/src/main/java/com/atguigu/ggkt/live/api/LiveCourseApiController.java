package com.atguigu.ggkt.live.api;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.live.service.LiveCourseService;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.utils.utils.AuthContextHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Date:  2022/9/22
 */
@RestController
@RequestMapping("api/live/liveCourse")
public class LiveCourseApiController {
    @Resource
    private LiveCourseService liveCourseService;
//    用户要观看直播，必须获取对应的用户access_token，通过access_token 获取观看的直播课程；
    @ApiOperation(value = "获取用户access_token")
    @GetMapping("getPlayAuth/{id}")
    public Result getPlayAuth(@PathVariable Long id) {
        JSONObject object = liveCourseService.getAccessToken(id , AuthContextHolder.getUserId());
        return Result.ok(object);
    }
}
