package com.atguigu.ggkt.vod.service;

import java.util.Map;

/**
 * Date:  2022/8/25
 */
public interface VodService {
//    上传视频
    String updateVideo();
//    删除视频
    void removeVideo(String fileId);
//    腾讯视频点播
    Map<String,Object> getPlayAuth(Long courseId, Long videoId);
}
