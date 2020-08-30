package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderNo);

    //根据订单号 查询订单的支付状态
    Map<String, String> queryPayStatus(String orderNo);

    //向支付表添加状态，更新支付信息
    void updateOrdersStatus(Map<String, String> map);
}
