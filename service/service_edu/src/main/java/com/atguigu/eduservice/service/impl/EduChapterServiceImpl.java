package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-08-01
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(eduChapterQueryWrapper);

        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideos = eduVideoService.list(eduVideoQueryWrapper);

        List<ChapterVo> finalList = new ArrayList<>();

        for (int i = 0; i < eduChapters.size(); i++) {
            //得到某一个一级分类
            EduChapter eduChapter = eduChapters.get(i);
            //建立（id，title）类的对象准备转换
            ChapterVo chapterVo = new ChapterVo();
            //将完整类对象转换为id，title的封装类
            BeanUtils.copyProperties(eduChapter, chapterVo);
            //新建个list准备存children对象集合
            List<VideoVo> videoVos = new ArrayList<>();
            //开始遍历所有二级分类
            for (int j = 0; j < eduVideos.size(); j++) {
                //获取某一个二级分类
                EduVideo eduVideo = eduVideos.get(j);
                //判断该二级分类的父id是否与该一级目录下的id相等
                if (eduVideo.getChapterId().equals(eduChapter.getId())) {
                    //建个（id，title）的简单类对象，准备封装
                    VideoVo videoVo = new VideoVo();
                    //将符合条件的一个二级目录完整类对象转化为自定义的简单（id，title）对象
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    //将该二级目录简单类对象放到list中
                    videoVos.add(videoVo);
                }
            }
            //将某一个一级目录的children属性赋值为刚才遍历得到的该一级目录的二级目录集合
            chapterVo.setChildren(videoVos);
            //将完整的ChapterVo放到一个集合中，作为返回值
            finalList.add(chapterVo);
        }
        return finalList;
    }

    //删除章节（如果章节里有video小节则不删除）
    @Override
    public boolean deleteChapter(String chapterId) {
        //查询video小节的对象，判断对象中chapterId是否是空 如果是空则删除chapterId所对应的chapter中Id对象
        QueryWrapper<EduVideo> eduVideoWrapper = new QueryWrapper<>();
        eduVideoWrapper.eq("chapter_id", chapterId);
        //只返回能查出记录数
        int count = eduVideoService.count(eduVideoWrapper);
        //判断count大于0说明有video值
        if (count > 0) {//说明查出了小节
            throw new GuliException(20001, "此章节存在小节不能删除");
        } else {
            int result = baseMapper.deleteById(chapterId);
            return result > 0;
        }

    }

    //根据course_id删除EduChapter
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id", courseId);
        baseMapper.delete(eduChapterQueryWrapper);
    }

}
