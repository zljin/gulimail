package com.zljin.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.order.entity.OrderEntity;
import com.zljin.gulimall.order.vo.OrderConfirmVo;
import com.zljin.gulimall.order.vo.OrderSubmitVo;
import com.zljin.gulimall.order.vo.SubmitOrderResponseVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:33:29
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);
}

