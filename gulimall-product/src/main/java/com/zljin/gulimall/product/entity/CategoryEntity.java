package com.zljin.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 商品三级分类
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-14 11:49:23
 */
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	private Long catId;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 父分类id
	 */
	private Long parentCid;
	/**
	 * 层级
	 */
	private Integer catLevel;
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
	private Integer showStatus;
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
	private String productUnit;
	/**
	 * 商品数量
	 */
	private Integer productCount;

	/**
	 * 子类别，ignore 表的field
	 */

	//@Leonard JsonInclude 若返回为空，则不展示children这个Item
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private List<CategoryEntity> children;

}
