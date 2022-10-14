package excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:  2022/8/17
 */
public class TestWrite {
    public static void main(String[] args) {
        //设置文件的路径
        String fileName = "E:\\ggkt.xlsx";
        //调用方法
        EasyExcel.write(fileName,User.class)
                .sheet("写操作")
                .doWrite(data());
    }

    private static List data() {
        List<User> list = new ArrayList<User>();
        for (int i = 1; i < 10; i++) {
            User data = new User();
            data.setId(i);
            data.setName("张三"+i);
            list.add(data);
        }
        return list;

    }
}
