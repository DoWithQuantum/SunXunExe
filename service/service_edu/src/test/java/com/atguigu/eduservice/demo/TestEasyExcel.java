package com.atguigu.eduservice.demo;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        //实现Excel写入操作

        //设置写入文件夹地址和excel文件名称
        String filename = "G:\\easyExcelTest\\testEasyExcel01.xlsx";

       /* EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());
    }
    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demodata = new DemoData();
            demodata.setSno(i);
            demodata.setSname("lucy" + i );
            list.add(demodata);
        }
        return list;*/


        EasyExcel.read(filename, DemoData.class, new ExcelListener()).sheet().doRead();
    }
}
