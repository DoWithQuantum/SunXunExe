package com.atguigu.eduservice.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoData {
    //Excel表头 第一列头
    @ExcelProperty(value = "学生编号", index = 0)
    private Integer sno;
    //Excel表头 第二列头
    @ExcelProperty(value = "学生姓名", index = 1)
    private String sname;
}
