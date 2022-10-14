package com.atguigu.ggkt.vod.lisener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.vo.vod.SubjectEeVo;
import com.atguigu.ggkt.vod.mapper.SubjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Date:  2022/8/17
 */
//@Componnet是用在类上，声明这个类是一个组件被spring管理起来
@Component
public class SubjectListener extends AnalysisEventListener<SubjectEeVo> {
    @Autowired
    private SubjectMapper dictMapper;
    //一行一行读取
    @Override
    public void invoke(SubjectEeVo subjectEeVo, AnalysisContext analysisContext) {
        //调用方法添加数据库
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectEeVo,subject);
        dictMapper.insert(subject);
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
