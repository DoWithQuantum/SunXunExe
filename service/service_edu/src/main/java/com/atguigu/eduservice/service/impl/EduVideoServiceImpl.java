package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-08-01
 */
////根据course_id删除EduVideo（可能多个）
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    //TODO 删除对应的视频文件
    @Autowired
    private VodClient vodClient;

    @Override
    public void removeVideoByCourseId(String courseId) {
        //根据课程id查询符合条件的视频源文件id
        QueryWrapper<EduVideo> eduVideoQueryWrapper1 = new QueryWrapper<>();
        eduVideoQueryWrapper1.eq("course_id", courseId);
        eduVideoQueryWrapper1.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(eduVideoQueryWrapper1);

        ArrayList<String> videoSourceIdList = new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            //判断eduVideo对象中的videoSourceId是否为空
            if (!StringUtils.isEmpty(eduVideo.getVideoSourceId())) {
                String videoSourceId = eduVideo.getVideoSourceId();
                videoSourceIdList.add(videoSourceId);
            }
        }
        //根据videoSourceIdList，用feign的service-vod中的方法删除视频
        //判断videoSourceIdList集合是否为空，不为空执行删除
        if (videoSourceIdList.size() > 0) {
            vodClient.deleteBatch(videoSourceIdList);
        }


        //删除小节对象（除视频）内容
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id", courseId);
        baseMapper.delete(eduVideoQueryWrapper);
    }
}
