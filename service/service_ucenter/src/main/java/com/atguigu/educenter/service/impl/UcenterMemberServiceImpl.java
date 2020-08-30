package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.eneity.RegisterVo;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-08-18
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    //注意泛型
    private RedisTemplate<String, String> redisTemplate;

    //登录
    @Override
    public String login(UcenterMember ucenterMember) {
        //获得用户输入的参数 ——用户名 手机号 密码
        String inputMobile = ucenterMember.getMobile();
        String inputNickname = ucenterMember.getNickname();
        String inputPassword = ucenterMember.getPassword();
        if (StringUtils.isEmpty(inputMobile) || StringUtils.isEmpty(inputPassword)) {
            throw new GuliException(20001, "账号或者密码为空");
        }
        //通过手机号查出对象
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("mobile", inputMobile);
        UcenterMember ucenterMember1 = baseMapper.selectOne(ucenterMemberQueryWrapper);
        //判断输入的用户名或者手机号是否存在
        if (ucenterMember1 == null) {
            throw new GuliException(20001, "账号不存在");
        }

        //判断密码是否正确
        //数据库密码为md5加密后的非明文
        //将用户输入的密码md5加密后与数据库进行比较
        if (!ucenterMember1.getPassword().equals(MD5.encrypt(inputPassword))) {
            throw new GuliException(20001, "密码不正确");
        }
        //判断账户是否禁用
        if (ucenterMember1.getIsDeleted()) {
            throw new GuliException(20001, "账号已被禁用，请联系管理员！");
        }

        //登录成功以后
        String jwtToken = JwtUtils.getJwtToken(ucenterMember1.getId(), ucenterMember1.getNickname());

        return jwtToken;
    }


    //注册
    @Override
    public void register(RegisterVo registerVo) {

        //获取用户输入的对象的各个字段
        String registerVoNickname = registerVo.getNickname();
        String registerVoMobile = registerVo.getMobile();
        String registerVoPassword = registerVo.getPassword();
        String registerVoCode = registerVo.getCode();

        //判断输入内容是否为空
        if (StringUtils.isEmpty(registerVoNickname) || StringUtils.isEmpty(registerVoMobile) || StringUtils.isEmpty(registerVoPassword) || StringUtils.isEmpty(registerVoNickname) || StringUtils.isEmpty(registerVoCode)) {
            throw new GuliException(20001, "信息输入有误！请检查！");
        }

        //手机号是否重复判断
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("mobile", registerVoMobile);
        Integer count = baseMapper.selectCount(ucenterMemberQueryWrapper);
        if (count > 0) {
            throw new GuliException(20001, "账户存在！");
        }

        //验证码是否正确判断
        String redisCode = redisTemplate.opsForValue().get(registerVoMobile);
        if (!redisCode.equals(registerVoCode)) {
            throw new GuliException(20001, "验证码验证错误");
        }

        //将用户输入的内容对象添加到数据库
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(registerVoMobile);//设置手机号
        ucenterMember.setNickname(registerVoNickname);//设置昵称
        ucenterMember.setPassword(MD5.encrypt(registerVoPassword));
        ucenterMember.setIsDisabled(false);
        ucenterMember.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597750069904&di=cf8e27e01711da4b0c4cdb69868275d7&imgtype=0&src=http%3A%2F%2Fdik.img.kttpdq.com%2Fpic%2F71%2F49311%2Fd50ba719275c01e6_1440x900.jpg");
        baseMapper.insert(ucenterMember);
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("openid", openid);
        UcenterMember ucenterMember = baseMapper.selectOne(ucenterMemberQueryWrapper);
        return ucenterMember;
    }

    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
