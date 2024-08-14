package com.zljin.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zljin.gulimall.product.entity.CategoryEntity;
import com.zljin.gulimall.product.service.CategoryService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.R;



/**
 * 商品三级分类
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 07:09:57
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list/tree")
    public R listWithTree(){
        return R.ok().put("data", categoryService.listWithTree());
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("id") Long id){
		CategoryEntity category = categoryService.getById(id);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 批量修改排序顺序
     * @param categorys
     * @return
     */
    @PutMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] categorys){
        categoryService.updateBatchById(Arrays.asList(categorys));
        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        categoryService.removeMenuByIds(Arrays.asList(ids));
        return R.ok();
    }

}
