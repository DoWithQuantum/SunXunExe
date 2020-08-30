package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmApiController {

    @Autowired
    private MsmService msmService;
    @Autowired //redis的核心对象
    private RedisTemplate<String, String> redisTemplate;

    //根据手机号发送验证码
    //从redis中获取验证码后往service中传值，设置有效时间
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) {

        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        code = RandomUtil.getFourBitRandom();
        HashMap<String, Object> param = new HashMap<>();
        param.put("code", code);
        //调用msmService启动阿里云查询发送验证码函数方法
        boolean issended = msmService.send(param, phone);
        if (issended) {
            //如果发送成功，将手机号和验证码一一对应放到redis中，并设置时间限制
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("发送验证码失败@+！");
        }
    }
}
