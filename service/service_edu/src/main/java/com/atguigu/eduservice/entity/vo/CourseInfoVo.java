package com.atguigu.eduservice.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseInfoVo {

    private String id;


    private String teacherId;


    private String subjectId;


    private String subjectParentId;//subjectParentId


    private String title;

    //价格精度精度
    private BigDecimal price;


    private Integer lessonNum;


    private String cover;


    private String description;
}