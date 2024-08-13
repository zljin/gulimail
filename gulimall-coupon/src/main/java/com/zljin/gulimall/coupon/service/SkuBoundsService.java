package com.zljin.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.coupon.entity.SkuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:04:12
 */
public interface SkuBoundsService extends IService<SkuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

