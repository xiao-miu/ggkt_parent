package com.atguigu.ggkt.vod.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.utils.exception.GgktException;
import com.atguigu.ggkt.vo.vod.SubjectEeVo;
import com.atguigu.ggkt.vod.lisener.SubjectListener;
import com.atguigu.ggkt.vod.mapper.SubjectMapper;
import com.atguigu.ggkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Date:  2022/8/16
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    @Resource
    private SubjectListener subjectListener;
    //课程分类列表
    //懒加载，每次查询一层数据
    @Override
    public List<Subject> selectSubjectList(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id",id);
        List<Subject> subjectList = baseMapper.selectList(wrapper);
        //subjectList遍历，得到每个subject对象，判断是否有下一层数据，有haschildren=true
        //向list集合每个Subject对象中设置hasChildren
        for (Subject subject:subjectList) {
            Long subjectId = subject.getId();
            //查询
            boolean isChild = this.isChildren(subjectId);
            subject.setHasChildren(isChild);
        }
        return subjectList;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try{
            //设置下载信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("课程分类", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

            //查询课程分类表所有数据
            List<Subject> list = baseMapper.selectList(null);
            //List<Subject> ---List<SubjectEeVo>
            ArrayList<SubjectEeVo> subjectEeVoList = new ArrayList<>();
            for (Subject subject : list) {
                SubjectEeVo subjectEeVo = new SubjectEeVo();
//                subjectEeVo.setId(subject.getId());
//                subjectEeVo.setParentId(subject.getParentId());
                BeanUtils.copyProperties(subject,subjectEeVo);
                subjectEeVoList.add(subjectEeVo);
            }

            EasyExcel.write(response.getOutputStream() , SubjectEeVo.class).sheet("课程分类").doWrite(subjectEeVoList);
        }catch(Exception e){
            throw new GgktException(20001,"导出失败");
        }
    }

    @Override
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream() ,SubjectEeVo.class , subjectListener).sheet().doRead();
        } catch (IOException e) {
            throw new GgktException(20001,"导入失败");
        }
    }

    private boolean isChildren(Long subjectId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",subjectId);
        Integer count = baseMapper.selectCount(queryWrapper);
        // 1>0trut    0>0 false

        return count > 0;
    }
}
