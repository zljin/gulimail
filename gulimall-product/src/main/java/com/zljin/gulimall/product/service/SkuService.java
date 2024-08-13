package com.zljin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.product.entity.SkuEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 07:09:57
 */
public interface SkuService extends IService<SkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

