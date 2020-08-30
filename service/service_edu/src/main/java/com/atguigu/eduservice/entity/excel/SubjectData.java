package com.atguigu.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

//与Excel表格对应的实体类
@Data
public class SubjectData {
    //初始化第一列表头
    @ExcelProperty(index = 0)
    private String oneSubjectName;
    //初始化第二页表头
    @ExcelProperty(index = 1)
    private String twoSubjectName;
}
