package com.zljin.gulimall.order.dao;

import com.zljin.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:33:29
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
