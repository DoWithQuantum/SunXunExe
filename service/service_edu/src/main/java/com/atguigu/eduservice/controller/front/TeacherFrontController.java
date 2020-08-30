package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/teacherfront")
@CrossOrigin
public class TeacherFrontController {
    @Autowired
    private EduTeacherService eduTeacherService;
    @Autowired
    private EduCourseService eduCourseService;

    //分页查询老师信息 返回分页的项目信息
    @GetMapping("getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontList(@PathVariable long page, @PathVariable long limit) {
        Page<EduTeacher> eduTeacherPage = new Page<>(page, limit);
        Map<String, Object> map = eduTeacherService.getTeacherFrontList(eduTeacherPage);
        return R.ok().data(map);
    }

    //查询老师信息及对应的课程信息
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId) {
        //根据老师id查出数据库老师对象信息
        EduTeacher eduTeacher = eduTeacherService.getById(teacherId);
        //
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
        eduCourseQueryWrapper.eq("teacher_id", teacherId);
        List<EduCourse> eduCourseList = eduCourseService.list(eduCourseQueryWrapper);

        return R.ok().data("eduTeacher", eduTeacher).data("eduCourseList", eduCourseList);
    }
}
