package com.atguigu.eduservice.client.Impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R removeAlyVideo(String id) {

        return R.error().message("删除单个视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {

        return R.error().message("删除多个视频出错了");
    }
}
