package excel;

import com.alibaba.excel.EasyExcel;

/**
 * Date:  2022/8/17
 */
public class TestRead {
    public static void main(String[] args) {
        String filename = "E:\\ggkt.xlsx";
        EasyExcel.read(filename,User.class,new ExcelListener()).sheet().doRead();
    }
}
