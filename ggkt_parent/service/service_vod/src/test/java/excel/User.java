package excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Date:  2022/8/17
 */
@Data
public class User {
//    index = 0  代表第一列
    @ExcelProperty(value = "用户id",index = 0)
    private int id;
//    index = 1  代表第二列
    @ExcelProperty(value = "用户姓名",index = 1)
    private String name;

}
