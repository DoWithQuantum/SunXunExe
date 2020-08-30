package com.atguigu.eduservice.entity.frontvo;

import lombok.Data;

//用于前端封装查询条件的类
@Data
public class CourseFrontVo {


    private String title;


    private String teacherId;


    private String subjectParentId;


    private String subjectId;


    private String buyCountSort;


    private String gmtCreateSort;


    private String priceSort;
}
