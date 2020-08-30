package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;

//不需要将返回的数据发送的页面 不用RestController
//@RestController
@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class WxApiController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //手机扫描二维码之后点同意后执行该方法
    @GetMapping("callback")
    public String callback(String code, String state) {
        try {
             /* System.out.println("code：" + code);
        System.out.println("state："+ state);*/
            //第二次发送请求地址模板
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //将数据填充二次请求获取用户信息验证的地址
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxUtils.APP_ID,
                    ConstantWxUtils.APP_SECRET,
                    code);
            //用httpclient工具类执行 返回的是访问验证令牌
            //返回的access_token是访问凭证 openid是微信用户唯一识别id
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //用GSON工具将凭证转换为map结构
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");


            UcenterMember ucenterMember = ucenterMemberService.getOpenIdMember(openid);
            if (ucenterMember == null) {
                //判断项目数据库中用户资料为空，所以需要从微信服务端将用户的信息查询出来
                //根据openid查找对象
                //设置提交access_token和openid
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap userMap = gson.fromJson(userInfo, HashMap.class);

                String nickname = (String) userMap.get("nickname");
                String headimgurl = (String) userMap.get("headimgurl");
                ucenterMember = new UcenterMember();
                ucenterMember.setAvatar(headimgurl);
                ucenterMember.setNickname(nickname);
                ucenterMember.setOpenid(openid);
                //将从微信服务端获取的用户信息保存到项目端的数据库中
                ucenterMemberService.save(ucenterMember);
            }
            //网页地址栏中返回token字符串 便于网页查询用户信息
            String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
            return "redirect:http://localhost:3000?token=" + jwtToken;

        } catch (Exception e) {
            throw new GuliException(20001, "微信登录错误！");
        }
    }

    //访问该方法跳转到生成的二维码页面
    @GetMapping("login")
    public String getWxCode() {
        String redirectUrl = ConstantWxUtils.REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (Exception e) {
            throw new GuliException(20001, e.getMessage());
        }
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String url = String.format(baseUrl, ConstantWxUtils.APP_ID, redirectUrl, "atguigu");

        return "redirect:" + url;
    }
}
