package com.zljin.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.product.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-14 11:49:23
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

