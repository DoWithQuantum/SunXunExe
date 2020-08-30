package com.atguigu.msmservice.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;

import com.atguigu.servicebase.config.exceptionhandler.GuliException;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    @Override
    public boolean send(HashMap<String, Object> param, String phone) {
        //如果号码为空 返回错误
        if (StringUtils.isEmpty(phone)) {
            return false;
        }

        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI4FzpdiX5N4yWC5tDtgdh", "rHO0fAuOpqde0KXTUHjwZskDdvfhYm");
        IAcsClient client = new DefaultAcsClient(profile);

        //设置固定参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置参数
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "布谷在线教育学习交流网站");
        request.putQueryParameter("TemplateCode", "SMS_199773996");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        //最终发送
        try {
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
