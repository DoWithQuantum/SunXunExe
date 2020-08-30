package com.atguigu.vod.service.Impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.atguigu.vod.Utils.InitVodClient;
import com.atguigu.vod.Utils.VodConstantUtil;
import com.atguigu.vod.service.VodService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {


    //上传文件
    @Override
    public String uploadVideoAly(MultipartFile file) {
        /**
         * 流式上传接口
         *
         * @param accessKeyId
         * @param accessKeySecret
         * @param title
         * @param fileName
         * @param inputStream
         */
        try {
            //获得原始文件名
            String originalFilename = file.getOriginalFilename();
            //获得文件流
            InputStream inputStream = file.getInputStream();
            //截取原文件名“。”之前的字段
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            //建立上传流请求request
            UploadStreamRequest request = new UploadStreamRequest(VodConstantUtil.KEY_ID, VodConstantUtil.KEY_SECRET, title, originalFilename, inputStream);
            //建立上传视频的实现类对象
            UploadVideoImpl uploader = new UploadVideoImpl();
            //通过实现类的对象将request请求返回response相应
            UploadStreamResponse response = uploader.uploadStream(request);


            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                System.out.print("VideoId=" + response.getVideoId() + "\n");
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
            //返回视频id
            return response.getVideoId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    //根据视频id删除多个视频
    @Override
    public void removeAlyVideos(List videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(VodConstantUtil.KEY_ID, VodConstantUtil.KEY_SECRET);
            //创建删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //用工具类将videoIdList转换为字符串
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");
            //向request中设置视频id
            request.setVideoIds(videoIds);
            //调用初始化对象的类执行方法
            client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除多个视频失败");
        }

    }
}
