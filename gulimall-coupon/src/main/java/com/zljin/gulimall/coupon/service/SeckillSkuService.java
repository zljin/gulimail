package com.zljin.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.coupon.entity.SeckillSkuEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:04:12
 */
public interface SeckillSkuService extends IService<SeckillSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

