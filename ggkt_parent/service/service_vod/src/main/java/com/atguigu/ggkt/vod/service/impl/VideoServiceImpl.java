package com.atguigu.ggkt.vod.service.impl;


import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.vod.mapper.VideoMapper;
import com.atguigu.ggkt.vod.service.VideoService;
import com.atguigu.ggkt.vod.service.VodService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-17
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    //注入小结
    @Resource
    private VodService vodService;
    @Override
    public void rumoveVideoByCourseId(Long id) {
        //根据课程id查询课程所有小节
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",id);  //coulumn 是根据数据库的字段名来的
        //得到所有小结
        List<Video> videos = baseMapper.selectList(videoQueryWrapper);
        for (Video video : videos) {
            //获取每个小节视频id
            String VodsourceId = video.getVideoSourceId();
            //判断视频id是否为空，不为空，删除腾讯云视频
            if (!StringUtils.isNotBlank(VodsourceId)) {
                //删除腾讯云视频
                vodService.removeVideo(VodsourceId);
            }
        }
//        根据课程id删除课程所有小节
        baseMapper.delete(videoQueryWrapper);
    }

    @Override
    public void removeVideoById(Long id) {
        //根据id查询小节
        Video video = baseMapper.selectById(id);
        //获取video里面视频id
        String videoSourceId = video.getVideoSourceId();
        //判断视频id是否为空
        if (videoSourceId != null && !videoSourceId.isEmpty()) {
            //视频id不为空,调用方法根据视频id删除腾讯云视频
            vodService.removeVideo(videoSourceId);
        }
        //根据id删除小结
        baseMapper.deleteById(id);
    }
}
