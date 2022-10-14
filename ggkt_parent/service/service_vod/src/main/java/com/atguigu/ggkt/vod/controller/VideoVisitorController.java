package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vod.service.VideoVisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-24
 */
@Api(tags = "课程统计功能")
@RestController
@RequestMapping("/admin/vod/videoVisitor")
//@CrossOrigin
public class VideoVisitorController {
    @Resource
    private VideoVisitorService videoVisitorService;

    @ApiOperation("显示统计数据")
    @GetMapping("findCount/{courseId}/{startDate}/{endDate}")
    public Result findCount(
            @ApiParam("查询课程的id") @PathVariable Long courseId,
            @ApiParam("开始时间") @PathVariable String startDate,
            @ApiParam("结束时间") @PathVariable String endDate){

        Map<String, Object> map = videoVisitorService.findCount(courseId, startDate, endDate);
        return Result.ok(map);
    }
}

