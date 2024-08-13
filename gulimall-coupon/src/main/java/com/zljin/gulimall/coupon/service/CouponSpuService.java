package com.zljin.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.coupon.entity.CouponSpuEntity;

import java.util.Map;

/**
 * 优惠券与产品关联
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:04:13
 */
public interface CouponSpuService extends IService<CouponSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

