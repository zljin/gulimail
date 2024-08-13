package com.zljin.gulimall.member.dao;

import com.zljin.gulimall.member.entity.UserAddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址表
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:14:36
 */
@Mapper
public interface UserAddressDao extends BaseMapper<UserAddressEntity> {
	
}
