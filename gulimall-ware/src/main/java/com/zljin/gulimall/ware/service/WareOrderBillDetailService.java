package com.zljin.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.ware.entity.WareOrderBillDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:38:34
 */
public interface WareOrderBillDetailService extends IService<WareOrderBillDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

