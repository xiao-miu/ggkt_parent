package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Date:  2022/8/16
 */
public interface SubjectService extends IService<Subject> {
    //查询查询下一层课程分类
    List<Subject> selectSubjectList(Long id);
    //课程分类导出
    void exportData(HttpServletResponse response);
    //课程分类导入
    void importDictData(MultipartFile file);
}
