package com.zljin.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.zljin.gulimall.common.valid.AddGroup;
import com.zljin.gulimall.common.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 07:09:58
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "修改必须指定品牌id",groups = UpdateGroup.class)
	@Null(message = "自增主键,新增不能指定id",groups = AddGroup.class)
	@TableId
	private Long id;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空",groups = {AddGroup.class,UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo
	 * @Leonard 可用oos将具体的图片存储到云端，oos功能略
	 */
	private String logo;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	private Integer status;
	/**
	 * 检索首字母
	 */
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotEmpty(groups = {AddGroup.class})
	@Min(value = 0,message = "排序必须大于等于0",groups = {AddGroup.class,UpdateGroup.class})
	private Integer sort;
	/**
	 * 备注
	 */
	private String remark;

}
