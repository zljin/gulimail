package com.zljin.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.coupon.entity.CouponSpuCategoryEntity;

import java.util.Map;

/**
 * 优惠券分类关联
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:04:13
 */
public interface CouponSpuCategoryService extends IService<CouponSpuCategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

