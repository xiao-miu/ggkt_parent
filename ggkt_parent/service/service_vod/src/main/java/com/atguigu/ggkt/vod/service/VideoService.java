package com.atguigu.ggkt.vod.service;


import com.atguigu.ggkt.model.vod.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-17
 */
public interface VideoService extends IService<Video> {
    //根据课程id删除小节
    void rumoveVideoByCourseId(Long id);
    //删除小节删除视频
    void removeVideoById(Long id);
}
