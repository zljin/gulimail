package com.zljin.gulimall.member.dao;

import com.zljin.gulimall.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-16 15:30:47
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
