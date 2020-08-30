package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-08-01
 */
public interface EduCourseService extends IService<EduCourse> {
    //保存前端传递的json格式的对象
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据id获得课程
    CourseInfoVo getCourseInfo(String courseId);

    //修改课程
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo getPublishCourse(String courseId);


    void removeCourseInfo(String courseId);

    Map<String, Object> getCourseFrontList(Page<EduCourse> eduCoursePage, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);
}
