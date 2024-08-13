package com.zljin.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.ware.entity.WareEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:38:33
 */
public interface WareService extends IService<WareEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

