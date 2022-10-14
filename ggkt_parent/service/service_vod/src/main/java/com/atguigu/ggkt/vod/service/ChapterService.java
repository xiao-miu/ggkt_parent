package com.atguigu.ggkt.vod.service;


import com.atguigu.ggkt.model.vod.Chapter;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-17
 */
public interface ChapterService extends IService<Chapter> {


    List<ChapterVo> getTreeList(Long id);
    //根据课程id删除章节
    void removeChapterById(Long id);

}
