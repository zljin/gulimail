package com.zljin.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 商品三级分类
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 07:09:57
 */
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	private Long id;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 父分类id
	 */
	private Long parentId;
	/**
	 * 是否显示[0-不显示，1显示]
	 * @leonard 用于逻辑删除，
	 * mybatis-plus @TableLogic 和下面的配置实现
	 *   global-config:
	 *     db-config:
	 *       id-type: auto
	 *       logic-delete-value: 1 # 逻辑删除
	 *       logic-not-delete-value: 0
	 */
	@TableLogic(value = "1",delval = "0")
	private Integer status;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 图标地址
	 */
	private String icon;
	/**
	 * 计量单位
	 */
	private String unit;

	/**
	 * 子类别，ignore 表的field
	 */
	@TableField(exist = false)
	private List<CategoryEntity> children;

}
