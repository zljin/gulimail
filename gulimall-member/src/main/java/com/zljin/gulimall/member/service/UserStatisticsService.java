package com.zljin.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.member.entity.UserStatisticsEntity;

import java.util.Map;

/**
 * 统计信息表
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:14:36
 */
public interface UserStatisticsService extends IService<UserStatisticsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

