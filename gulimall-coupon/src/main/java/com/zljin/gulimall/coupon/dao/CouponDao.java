package com.zljin.gulimall.coupon.dao;

import com.zljin.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:04:13
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
