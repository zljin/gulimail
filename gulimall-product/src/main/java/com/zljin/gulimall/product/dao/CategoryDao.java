package com.zljin.gulimall.product.dao;

import com.zljin.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-14 11:49:23
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
