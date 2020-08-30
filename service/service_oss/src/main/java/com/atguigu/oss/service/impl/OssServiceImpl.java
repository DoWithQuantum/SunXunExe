package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        try {
            // Endpoint以杭州为例，其它Region请按实际情况填写。
            String endpoint = ConstantPropertiesUtils.END_POINT;
            String accessKeyId = ConstantPropertiesUtils.KEY_ID;
            String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
            String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取文件项目名称
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //joda-time依赖包中的工具类
            String datetime = new DateTime().toString("yyyy/MM/dd");
            //在OSS服务器上建立时间文件目录+uuid+上传时的文件名
            fileName = datetime + "/" + uuid + fileName;

            // 上传文件流。
            InputStream fileInputStream = file.getInputStream();

            //执行上传方法
            ossClient.putObject(bucketName, fileName, fileInputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //拼接oss路径 并返回   注意https
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
