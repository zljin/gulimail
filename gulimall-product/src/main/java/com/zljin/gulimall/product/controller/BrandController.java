package com.zljin.gulimall.product.controller;

import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.R;
import com.zljin.gulimall.product.entity.BrandEntity;
import com.zljin.gulimall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



/**
 * 品牌
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 07:09:58
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("id") Long id){
		BrandEntity brand = brandService.getById(id);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody @Valid BrandEntity brand/*, BindingResult result*/){
/*        if(result.hasErrors()){
            Map<String,String> map = new HashMap<>();
            result.getFieldErrors().forEach(t->{
                map.put(t.getField(),t.getDefaultMessage());
            });
            R.error(400,"提交数据不合法").put("data",map);
        }else {
            brandService.save(brand);
        }*/
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] ids){
		brandService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
