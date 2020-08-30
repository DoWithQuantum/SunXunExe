package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    //注入service
    @Autowired
    private OssService ossService;

    //接口
    @PostMapping("uploadOssFile")
    public R uploadOssFile(MultipartFile file) {
        //方法返回保存图片的地址拼接
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url", url);
    }
}
