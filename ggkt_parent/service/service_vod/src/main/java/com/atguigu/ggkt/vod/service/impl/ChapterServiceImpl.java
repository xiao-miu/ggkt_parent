package com.atguigu.ggkt.vod.service.impl;


import com.atguigu.ggkt.model.vod.Chapter;
import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vo.vod.VideoVo;
import com.atguigu.ggkt.vod.mapper.ChapterMapper;
import com.atguigu.ggkt.vod.service.ChapterService;
import com.atguigu.ggkt.vod.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-17
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Resource
    private VideoService videoService;
    //大纲列表
    @Override
    public List<ChapterVo> getTreeList(Long id) {
        ArrayList<ChapterVo> finalList = new ArrayList<>();
        //根据courseId获取课程里面所有章节
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",id);
        queryWrapper.orderByAsc("sort","id");
        List<Chapter> chapterList = baseMapper.selectList(queryWrapper);
        //根据courseId获取课程里面所有小节(换一种写法)
        LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Video::getCourseId , id);
        lambdaQueryWrapper.orderByAsc(Video::getSort , Video::getId);
        List<Video> list = videoService.list(lambdaQueryWrapper);
        //封装章节
        for (int i = 0 ;i<chapterList.size() ; i++){
            Chapter chapter = chapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            //得到每个章节对象放到inalChapterList集合
            finalList.add(chapterVo);
            //封装章节里面小节
            ArrayList<VideoVo> videoVoList = new ArrayList<>();
            for (Video vo :list){
                //判断小节是哪个章节下面
                // 章节id和小节chapter_id
                if(chapter.getId().equals(vo.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(vo,videoVo);
                    //放到videoVoList里
                    videoVoList.add(videoVo);
                }
            }
            ///把章节里面所有小节集合放到每个章节里面
            chapterVo.setChildren(videoVoList);
        }


        return finalList;
    }

    @Override
    public void removeChapterById(Long id) {
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",id);
        baseMapper.delete(chapterQueryWrapper);
    }

}
