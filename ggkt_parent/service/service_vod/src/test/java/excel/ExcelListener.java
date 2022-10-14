package excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * Date:  2022/8/17
 */
public class ExcelListener extends AnalysisEventListener<User> {
    //一行一行读取excel内容，把每行内容封装到user对象
    //从excel第二行开始读取
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println(user);
    }
    //读取表头操作
    @Override
    public void invokeHeadMap(Map headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
        System.out.println("表头："+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
