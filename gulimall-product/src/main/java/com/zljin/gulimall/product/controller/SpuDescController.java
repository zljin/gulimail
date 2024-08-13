package com.zljin.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zljin.gulimall.product.entity.SpuDescEntity;
import com.zljin.gulimall.product.service.SpuDescService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.R;



/**
 * spu信息介绍
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 07:09:57
 */
@RestController
@RequestMapping("product/spudesc")
public class SpuDescController {
    @Autowired
    private SpuDescService spuDescService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:spudesc:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuDescService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{spuId}")
    //@RequiresPermissions("product:spudesc:info")
    public R info(@PathVariable("spuId") Long spuId){
		SpuDescEntity spuDesc = spuDescService.getById(spuId);

        return R.ok().put("spuDesc", spuDesc);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:spudesc:save")
    public R save(@RequestBody SpuDescEntity spuDesc){
		spuDescService.save(spuDesc);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spudesc:update")
    public R update(@RequestBody SpuDescEntity spuDesc){
		spuDescService.updateById(spuDesc);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spudesc:delete")
    public R delete(@RequestBody Long[] spuIds){
		spuDescService.removeByIds(Arrays.asList(spuIds));

        return R.ok();
    }

}
