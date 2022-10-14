package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vod.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * Date:  2022/8/16
 */
@Api(tags = "课程分类管理")
@RestController
@RequestMapping("/admin/vod/subject/")
//@CrossOrigin    //为了解决跨域问题
public class SubjectController {
    @Resource
    private SubjectService subjectService;
    //查询下一层课程分类
    //根据parent_id
    @ApiOperation("查询下一层的课程分类")
    @GetMapping("getChildSubject/{id}")
    public Result getChildSubject(@PathVariable Long id) {
        List<Subject> list = subjectService.selectSubjectList(id);
        return Result.ok(list);
    }
    @ApiOperation("课程分类导出")
    @GetMapping("exportData")//HttpServletRequest下载
    public void exportData(HttpServletResponse response){
        subjectService.exportData(response);
    }
    @ApiOperation("课程分类导入")
    @PostMapping("importData")
    public Result importData(MultipartFile file){
       subjectService.importDictData(file);
        return Result.ok(null);
    }
}
