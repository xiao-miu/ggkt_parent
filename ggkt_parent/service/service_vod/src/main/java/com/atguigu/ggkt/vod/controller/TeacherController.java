package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vo.vod.TeacherQueryVo;
import com.atguigu.ggkt.vod.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-11
 */
//讲师的增删改查接口
@RestController
@RequestMapping("/admin/vod/teacher/")
//@CrossOrigin    //为了解决跨域问题
public class TeacherController {
    @Resource
    private TeacherService teacherService;
    @ApiOperation("查询所有讲师")
    //查询所有讲师
    @GetMapping("allTeacher")
    public Result getTeachers() {
        List<Teacher> list = teacherService.list();
//        int i = 10/ 0;
//        return Result.ok(list);
        return Result.ok(list).message("查询成功");
    }
    @ApiOperation("根据id删除讲师")
    @DeleteMapping("remove/{id}")
    public Result deleteTeacher(@PathVariable Long id) {
        boolean deleteTeacher = teacherService.removeById(id);
        if(deleteTeacher){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
/*
 @RequestBody  接收传入的json数据   配合着post来使用
 @ResposeBody  响应数据
 */
    @ApiOperation("条件分页查询")
    @PostMapping("findQueryPage/{current}/{limit}")
//    @GetMapping("findQueryPage/{current}/{limit}")    //postman用get的方式可以请求成功
    //@RequestBody(required = false) TeacherQueryVo teacherQueryVo
    //1、提交参数以json格式提交? key=value&key=value的那种形式是拿不到数据的
    //2、条件值可以为空
    //3、和post提交一起使用Get是拿不到的
    public Result findQueryPage(@PathVariable Long current , @PathVariable Long limit , @RequestBody(required = false) TeacherQueryVo teacherQueryVo) {
        Page<Teacher> page = new Page<>(current,limit);
        //判断讲师表里是否为空
        if(teacherQueryVo == null){
            Page<Teacher> pageModel = teacherService.page(page,null);
            return Result.ok(pageModel);
        }else {
            //获取条件值
            String name = teacherQueryVo.getName();
            Integer level = teacherQueryVo.getLevel();
            String joinDateBegin = teacherQueryVo.getJoinDateBegin();
            String joinDateEnd = teacherQueryVo.getJoinDateEnd();
            //进行非空判断，条件封装
            QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
            if(!StringUtils.isEmpty(name)){
                wrapper.like("name",name);
            }
            if(!StringUtils.isEmpty(level)){
                wrapper.eq("level",level);
            }
            if(!StringUtils.isEmpty(joinDateBegin)){
                wrapper.ge("join_date",joinDateBegin);
            }
            if(!StringUtils.isEmpty(joinDateEnd)){
                wrapper.le("join_date",joinDateEnd);
            }
            //调用方法分页查询
            Page<Teacher> pageModel = teacherService.page(page,wrapper);
            return Result.ok(pageModel);
        }
    }
    @ApiOperation("添加讲师")
    @PostMapping("saveTeacher")
    public Result saveTeacher(@RequestBody Teacher teacher){
        boolean save = teacherService.save(teacher);
        if(save){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    //先查询后修改
    @ApiOperation("修改根据id查询")
    @GetMapping("getTeacher/{id}")
    public Result getTeacher(@PathVariable Long id){
        Teacher byId = teacherService.getById(id);
        return Result.ok(byId);
    }
    @ApiOperation("修改最终实现的数据")
    @PostMapping("updateTeacher")
    public Result updateTeacher(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.updateById(teacher);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    @ApiOperation("批量删除讲师")
    @DeleteMapping("removeTeachers")
    public Result removeTeacher(@RequestBody List<Long> id){
        boolean isSuccess = teacherService.removeByIds(id);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    @ApiOperation("根据id查询")
    @GetMapping("inner/getTeacher/{id}")
    public Teacher getTeacherLive(@PathVariable Long id) {
        Teacher teacher = teacherService.getById(id);
        return teacher;
    }
}

