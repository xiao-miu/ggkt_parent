package com.atguigu.ggkt.live.controller;


import com.atguigu.ggkt.live.service.LiveCourseAccountService;
import com.atguigu.ggkt.live.service.LiveCourseDescriptionService;
import com.atguigu.ggkt.live.service.LiveCourseService;
import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.model.live.LiveCourseAccount;
import com.atguigu.ggkt.model.live.LiveCourseConfig;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import sun.plugin2.main.client.LiveConnectSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-18
 */
@RestController
@RequestMapping("/admin/live/liveCourse")
public class LiveCourseController {
    @Resource
    private LiveCourseService liveCourseService;
    @Resource
    private LiveCourseAccountService liveCourseAccountService;

    @ApiOperation(value = "获取最近的直播")
    @GetMapping("findLatelyList")
    public Result findLatelyList() {
        List<LiveCourseVo> list = liveCourseService.findLatelyList();
        return Result.ok(list);
    }

//修改直播配置信息
    @ApiOperation(value = "修改配置")
    @PutMapping("updateConfig")
    public Result updateConfig(@RequestBody LiveCourseConfigVo liveCourseConfigVo) {
        liveCourseService.updateConfig(liveCourseConfigVo);
        return Result.ok();
    }

//    获取直播账号信息
    @ApiOperation(value = "获取直播账号信息")
    @GetMapping("getLiveCourseAccount/{id}")
    public Result<LiveCourseAccount> getLiveCourseAccount(@PathVariable Long id) {
        liveCourseAccountService.getByLiveCourseId(id);
        return Result.ok();
    }

    @ApiOperation(value = "获取直播配置信息")
    @GetMapping("getCourseConfig/{id}")
    public Result<LiveCourseConfigVo> getCourseConfig(@PathVariable Long id) {

        return Result.ok(liveCourseService.getCourseConfig(id));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        liveCourseService.removeLive(id);
        return Result.ok();
    }

    @ApiOperation(value = "直播课程添加")
    @PostMapping("save")
    public Result save(@RequestBody LiveCourseFormVo liveCourseFormVo){
        liveCourseService.saveLive(liveCourseFormVo);
        return Result.ok();
    }

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<LiveCourse> pageParam = new Page<>(page, limit);
        IPage<LiveCourse> pageModel = liveCourseService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    //修改信息
    //id查询直播课程基本信息
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        LiveCourse byId = liveCourseService.getById(id);
        return Result.ok(byId);
    }
    //id查询直播课程基本信息和描述信息
    @ApiOperation(value = "获取")
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id) {
       LiveCourseFormVo liveCourseFormVo = liveCourseService.getLiveCourseVo(id);
       return Result.ok(liveCourseFormVo);
    }
    //更新直播课程方法
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody LiveCourseFormVo liveCourseVo) {
        liveCourseService.updateLiveById(liveCourseVo);
        return Result.ok();
    }
}

