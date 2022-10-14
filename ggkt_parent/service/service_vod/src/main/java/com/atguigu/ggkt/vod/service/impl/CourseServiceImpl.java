package com.atguigu.ggkt.vod.service.impl;


import com.atguigu.ggkt.model.vod.*;
import com.atguigu.ggkt.vo.vod.*;
import com.atguigu.ggkt.vod.mapper.CourseMapper;
import com.atguigu.ggkt.vod.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-17
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Resource
    private TeacherService teacherService;
    @Resource
    private SubjectService subjectService;
    @Resource
    private CourseDescriptionService courseDescriptionService;
    @Resource
    private VideoService videoService;    //视屏（小结）
    @Resource
    private ChapterService chapterService;        //章节
    @Override
    public Map<String, Object> findPageCourse(Page<Course> pages, CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();  //获取课程标题
        Long subjectId = courseQueryVo.getSubjectId();//第一层分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();//第二层分类
        Long teacherId = courseQueryVo.getTeacherId();//获取讲师id

        //判断条件为空，封装条件
        //封装条件
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)) {
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id",teacherId);
        }
        //调用方法实现条件分页查询
        Page<Course> coursePage = baseMapper.selectPage(pages, wrapper);
        long totalCount = coursePage.getTotal();//总记录数
        long pagePages = coursePage.getPages();//总页数
        long currentPage = pages.getCurrent();//当前页
        long size = pages.getSize();//每页记录数
        List<Course> records = coursePage.getRecords(); //每页数据集合

        //查询数据里面有几个id
        // 讲师id课程分类id (一层和二层）
        //获取这些id对应名称，进行封装，最终显示I
        records.stream().forEach(item -> {
            this.getNameById(item);
                });

        //封装返回数据
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);//总记录数
        map.put("totalPage",pagePages);    //总页数
        map.put("records",records);     //
        return map;
    }
    //添加课程信息
    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        //添加课程基本信息，操作course表
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.insert(course);
        //添加课程描述信息，操作course_description
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());
        courseDescription.setCourseId(course.getId());
        courseDescriptionService.save(courseDescription);

        return course.getId();
    }

    @Override
    public CourseFormVo getCourseFormVoById(Long id) {
        //课程基本信息
        Course course = baseMapper.selectById(id);
        if(course == null){
            return null;
        }
        //课程描述信息
        QueryWrapper<CourseDescription> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", id);
        CourseDescription courseDescription = courseDescriptionService.getOne(wrapper);
//        CourseDescription courseDescription = courseDescriptionService.getById(id);
        //封装基本信息
        CourseFormVo courseFormVo = new CourseFormVo();
        BeanUtils.copyProperties(course,courseFormVo);
        //封装描述信息
        if(courseDescription != null){
            courseFormVo.setDescription(courseDescription.getDescription());
        }
        return courseFormVo;
    }

    @Override
    public void updateCourseById(CourseFormVo courseFormVo) {
        //修个课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.updateById(course);

        //修改课程信息
        CourseDescription description = new CourseDescription();
//        CourseDescription description = courseDescriptionService.getById(course.getId());
        description.setDescription(courseFormVo.getDescription());
        description.setId(course.getId());
        courseDescriptionService.updateById(description);
    }

    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public void publishCourseById(Long id) {
        Course course = baseMapper.selectById(id);
        course.setStatus(1);
        course.setPublishTime(new Date());
        baseMapper.updateById(course);
    }

    @Override
    public void removeCourseById(Long id) {
        //根据课程id删除小节
        videoService.rumoveVideoByCourseId(id);
        //根据课程id删除章节
        chapterService.removeChapterById(id);
        //根据课程id删除课程描述
        courseDescriptionService.removeById(id);
        //根据课程id删除课程
        baseMapper.deleteById(id);
    }
    //根据课程分类查询课程列表（分页)
    @Override
    public Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();//课程名称
        Long subjectId = courseQueryVo.getSubjectId();//二级分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();//一级分类
        Long teacherId = courseQueryVo.getTeacherId();//讲师
        //判断条件值是否为空，封装
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)) {
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id",teacherId);
        }
        //调用方法进行条件分页查询
        Page<Course> coursePage = baseMapper.selectPage(pageParam, wrapper);
        long totalCount = coursePage.getTotal();//总记录数
        long totalPage = coursePage.getPages();//总页数
        long currentPage = coursePage.getCurrent();//当前页
        long size = coursePage.getSize();//每页记录数
        //每页数据集合
        List<Course> records = coursePage.getRecords();
        //获取数据
        //封装其他数据（获取讲师名称和课程分类名称)
        records.stream().forEach(item -> {
            this.getTeacherOrSubjectName(item);
        });
//       返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);
        return map;
    }
//获取讲师名称和课程分类名称
    private Course getTeacherOrSubjectName(Course course) {
        //获取讲师名称
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if(teacher != null) {
            course.getParam().put("teacherName",teacher.getName());
        }

        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if(subjectOne != null) {
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if(subjectTwo != null) {
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return course;

    }

    //根据ID查询课程
    @Override
    public Map<String, Object> getByInfo(Long courseId) {
        //view_count流量数量+1
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);
        //根据课程id查询
        //课程详情数据
        CourseVo selectCourseVoById = baseMapper.selectCourseVoById(courseId);
        //课程分类数据
        List<ChapterVo> chapterVoList = chapterService.getTreeList(courseId);
        //课程描述信息
        CourseDescription courseDescriptionId = courseDescriptionService.getById(courseId);
        //课程所属讲师信息
        Teacher teacherById = teacherService.getById(course.getTeacherId());
        //封住map集合
        HashMap<String, Object> map = new HashMap<>();
        //TODO后续完善
        Boolean isBuy = false;

        map.put("courseVo", selectCourseVoById);
        map.put("chapterVoList", chapterVoList);
        map.put("description", null != courseDescriptionId ?
                courseDescriptionId.getDescription() : "");
        map.put("teacher", teacherById);
        map.put("isBuy", isBuy);//是否购买

        return map;
    }
    //查询所有课程
    @Override
    public List<Course> findlist() {
        List<Course> courses = baseMapper.selectList(null);
        for (Course item : courses) {
            this.getTeacherOrSubjectName(item);
        }
        return courses;
    }

    private Course getNameById(Course item) {
        Teacher teacher = teacherService.getById(item.getTeacherId());
        if(teacher != null){
            String name = teacher.getName();
            item.getParam().put("teacherName", name);
        }
        //根据课程分类id获取课程分类名称
        //查询讲师名称
        Subject subjectOne = subjectService.getById(item.getSubjectParentId());
        if(subjectOne != null) {
            item.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        Subject subjectTwo = subjectService.getById(item.getSubjectId());
        if(subjectTwo != null) {
            item.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return item;
    }
}
