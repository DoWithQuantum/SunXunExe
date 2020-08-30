package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
@RestController
@RequestMapping("/eduorder/order/")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    //生成订单
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request) {

        String orderNo = orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderNo", orderNo);
    }

    //根据订单号查询订单信息
    @GetMapping("getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);
        Order orderInfo = orderService.getOne(orderQueryWrapper);
        return R.ok().data("orderInfo", orderInfo);
    }

    //根据课程id和用户id查询订单表中的详情
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("course_id", courseId);
        orderQueryWrapper.eq("member_id", memberId);
        orderQueryWrapper.eq("status", 1);
        int count = orderService.count(orderQueryWrapper);

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }
}

