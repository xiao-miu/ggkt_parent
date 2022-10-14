package com.atguigu.ggkt.vod.service;


import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vo.vod.CourseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-17
 */
public interface CourseService extends IService<Course> {

//    获取分页列表
    Map<String, Object> findPageCourse(Page<Course> pages, CourseQueryVo courseQueryVo);
//    添加课程信息
    Long saveCourseInfo(CourseFormVo courseQueryVo);
//    根据id查询课程信息
    CourseFormVo getCourseFormVoById(Long id);
//    修改课程信息
    void updateCourseById(CourseFormVo courseFormVo);
//    根据课程id查询发布课程信息(三表联查)
    CoursePublishVo getCoursePublishVo(Long id);
//    根据id发布课程的最终发布
    void publishCourseById(Long id);
//    根据课程的id删除课程信息
    void removeCourseById(Long id);
    //根据课程分类查询课程列表（分页)
    Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);
    //根据ID查询课程
    Map<String, Object> getByInfo(Long courseId);
    //查询所有课程
    List<Course> findlist();
}

