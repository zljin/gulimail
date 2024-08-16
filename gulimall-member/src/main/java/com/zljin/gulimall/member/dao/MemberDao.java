package com.zljin.gulimall.member.dao;

import com.zljin.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-16 15:30:47
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
