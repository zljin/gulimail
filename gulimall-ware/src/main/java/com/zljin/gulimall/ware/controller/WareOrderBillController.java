package com.zljin.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zljin.gulimall.ware.entity.WareOrderBillEntity;
import com.zljin.gulimall.ware.service.WareOrderBillService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.R;



/**
 * 库存工作单
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:38:33
 */
@RestController
@RequestMapping("ware/wareorderbill")
public class WareOrderBillController {
    @Autowired
    private WareOrderBillService wareOrderBillService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:wareorderbill:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareOrderBillService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:wareorderbill:info")
    public R info(@PathVariable("id") Long id){
		WareOrderBillEntity wareOrderBill = wareOrderBillService.getById(id);

        return R.ok().put("wareOrderBill", wareOrderBill);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:wareorderbill:save")
    public R save(@RequestBody WareOrderBillEntity wareOrderBill){
		wareOrderBillService.save(wareOrderBill);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:wareorderbill:update")
    public R update(@RequestBody WareOrderBillEntity wareOrderBill){
		wareOrderBillService.updateById(wareOrderBill);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:wareorderbill:delete")
    public R delete(@RequestBody Long[] ids){
		wareOrderBillService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
