package com.atguigu.ggkt.vod.course;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.model.vod.Teacher;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Date:  2022/9/4
 */
@FeignClient(value = "service-vod")
public interface CourseFeignClient {
//    根据关键字查询课程
    @GetMapping("/api/vod/course/inner/findByKeyword/{keyword}")
    public List<Course> findByKeyword(@PathVariable String keyword);
    //公众号二级菜单跳转页面
    @GetMapping("/api/vod/course/inner/getById/{courseId}")
    public Course getById(@PathVariable Long courseId);
    //根据id查询讲师信息
    @GetMapping("/api/vod/course/inner/getTeacher/{id}")
    public Teacher getTeacherLive(@PathVariable Long id);
}

