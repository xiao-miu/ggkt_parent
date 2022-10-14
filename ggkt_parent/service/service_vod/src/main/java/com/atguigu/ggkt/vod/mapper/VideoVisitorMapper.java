package com.atguigu.ggkt.vod.mapper;

import com.atguigu.ggkt.model.vod.VideoVisitor;
import com.atguigu.ggkt.vo.vod.VideoVisitorCountVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 视频来访者记录表 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-08-24
 */
public interface VideoVisitorMapper extends BaseMapper<VideoVisitor> {
    //@Param给字段起别名
    //如果需要获取多个参数需要约定一个名字
    List<VideoVisitorCountVo> findCount(@Param("courseId") Long courseId,
                                        @Param("startDate")String startDate,
                                        @Param("endDate") String endDate);
}
