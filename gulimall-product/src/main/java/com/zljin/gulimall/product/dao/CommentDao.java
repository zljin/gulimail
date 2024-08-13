package com.zljin.gulimall.product.dao;

import com.zljin.gulimall.product.entity.CommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 07:09:57
 */
@Mapper
public interface CommentDao extends BaseMapper<CommentEntity> {
	
}
