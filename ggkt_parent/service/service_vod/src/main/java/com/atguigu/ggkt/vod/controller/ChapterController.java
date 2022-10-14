package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Chapter;
import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vod.service.ChapterService;
import com.atguigu.ggkt.vod.service.CourseService;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-17
 */
//实现课程章节的列表、添加、修改和删除功能
@Api(tags = "课程章节")
@RestController
@RequestMapping("/admin/vod/chapter")
//@CrossOrigin
public class ChapterController {
    @Resource()
    private ChapterService chapterService;

    //1大纲列表（章节和小节列表)
    @ApiOperation(value = "大纲列表")
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getNestedTreeList(@PathVariable Long courseId){
        List<ChapterVo> list = chapterService.getTreeList(courseId);
        return Result.ok(list);
    }
    //2添加章节
    @ApiOperation(value = "添加章节")
    @PostMapping("save")
    public Result save(@RequestBody Chapter chapter){
        chapterService.save(chapter);
        return Result.ok();
    }
    //3 修改-根据id查询
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Chapter chapter = chapterService.getById(id);
        return Result.ok(chapter);
    }
    //4 修改-最终实现
    @ApiOperation(value = "查询修改的信息")
    @GetMapping("update")
    public Result update(@PathVariable Long id) {
        Chapter byId = chapterService.getById(id);
        return Result.ok(byId);
    }
    //5删除章节
    @ApiOperation(value = "删除章节信息")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        chapterService.removeById(id);
        return Result.ok();
    }

}

