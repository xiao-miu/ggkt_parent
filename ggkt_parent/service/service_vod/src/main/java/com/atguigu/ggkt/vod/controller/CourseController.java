package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vod.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-17
 */
@Api(tags = "课程管理接口")
@RestController
@RequestMapping("/admin/vod/course/")
//@CrossOrigin    //为了解决跨域问题
//课程模块
public class CourseController {
    @Resource
    private CourseService courseService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page, //当前页
                        @PathVariable Long limit, //每页记录数
                        CourseQueryVo courseQueryVo) {
        Page<Course> pages = new Page<>(page, limit);
        Map<String, Object> map = courseService.findPageCourse(pages,courseQueryVo);
        return Result.ok(map);
    }
    @ApiOperation(value = "添加课程信息")
    @PostMapping("save")
    public Result save(@RequestBody CourseFormVo courseQueryVo){
        Long count = courseService.saveCourseInfo(courseQueryVo);
        return Result.ok(count);
    }

    //修改课程信息
    @ApiOperation(value="根据id获取课程信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        CourseFormVo course = courseService.getCourseFormVoById(id);
        return Result.ok(course);
    }
    @ApiOperation(value="根据id来修改课程信息")
    @PostMapping("update")
    public Result updateById(@RequestBody CourseFormVo courseFormVo) {
        courseService.updateCourseById(courseFormVo);
        return Result.ok(courseFormVo.getId());  //把id返回
    }
    @ApiOperation(value = "根据课程id查询发布课程信息")
    @GetMapping("getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@PathVariable Long id){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(id);
        return Result.ok(coursePublishVo);
    }
    @ApiOperation(value = "根据id发布课程的最终发布")
    @PutMapping("publishCourseById/{id}")
    public Result publishCourseById(@PathVariable Long id){
        courseService.publishCourseById(id);
        return Result.ok();
    }
    @ApiOperation(value="删除课程")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        courseService.removeCourseById(id);
        return Result.ok();
    }
    //查询所有课程
    @GetMapping("findAll")
    public Result findAll() {
        List<Course> list = courseService.findlist();
        return Result.ok(list);
    }
}

