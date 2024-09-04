package com.zljin.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zljin.gulimall.common.exception.BizCodeEnum;
import com.zljin.gulimall.common.exception.NoStockException;
import com.zljin.gulimall.common.to.SkuHasStockVo;
import com.zljin.gulimall.ware.vo.WareSkuLockVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zljin.gulimall.ware.entity.WareSkuEntity;
import com.zljin.gulimall.ware.service.WareSkuService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.R;



/**
 * 商品库存
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-15 21:11:19
 */
@Api(tags = "商品库存")
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;


    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo vo){
        try{
            Boolean stock = wareSkuService.orderLockStock(vo);
            return R.ok();
        }catch (NoStockException e){
            return R.error(BizCodeEnum.NO_STOCK_EXCEPTION.getCode(),e.getMessage());
        }
    }


    /**
     * 查询sku是否有库存,给商品上架用来远程调用
     * @param skuIds
     * @return
     */
    @PostMapping("/hasstock")
    public R getSkusHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockVo> vos =  wareSkuService.getSkusHasStock(skuIds);
        return R.ok().setData(vos);
    }

    /**
     * 列表
     */
    @ApiImplicitParam(name = "params")
    @ApiOperation(value = "查询商品库存")
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
