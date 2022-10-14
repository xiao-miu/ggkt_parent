package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.VideoVisitor;
import com.atguigu.ggkt.vo.vod.VideoVisitorCountVo;
import com.atguigu.ggkt.vod.mapper.VideoVisitorMapper;
import com.atguigu.ggkt.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-24
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {

    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {
        List<VideoVisitorCountVo> videoVisitorList = baseMapper.findCount(courseId, startDate, endDate);
        //创建map集合
        HashMap<String, Object> map = new HashMap<>();
        //创建两个List集合，一个代表所有日期，一个代表日期对应数量
        List<Date> dateList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        //封装        把数据通过map遍历成list集合存储到集合了
        dateList = videoVisitorList.stream().map(VideoVisitorCountVo :: getJoinTime).collect(Collectors.toList());
        countList = videoVisitorList.stream().map(VideoVisitorCountVo :: getUserCount).collect(Collectors.toList());
        //放到map集合
        map.put("xData", dateList);
        map.put("yData", countList);
        return map;
    }
}
