package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-08-01
 */
@RestController
@RequestMapping("/eduservice/eduvideo")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        boolean b = eduVideoService.save(eduVideo);
        return R.ok();
    }

    //删除小节
    //TODO 需要完善
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id) {
        //根据id查询video对象
        EduVideo eduVideo = eduVideoService.getById(id);
        //根据video对象得到videoId
        String videoSourceId = eduVideo.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)) {
            //调用微服务接口的service根据videoId删除视频
            R result = vodClient.removeAlyVideo(videoSourceId);
            if (result.getCode() == 20001) {
                throw new GuliException(20001, "*****删除视频熔断*****");
            }
        }
        //根据videoId删除小节对象
        eduVideoService.removeById(id);
        return R.ok();
    }

    //修改小节 TODO
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        boolean b = eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    @GetMapping("getVideo/{videoId}")
    public R getVideo(@PathVariable String videoId) {
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return R.ok().data("eduVideo", eduVideo);
    }
}

