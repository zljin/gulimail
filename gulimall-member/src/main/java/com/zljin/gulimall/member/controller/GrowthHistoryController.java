package com.zljin.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zljin.gulimall.member.entity.GrowthHistoryEntity;
import com.zljin.gulimall.member.service.GrowthHistoryService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.R;



/**
 * 成长积分记录表
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:14:36
 */
@RestController
@RequestMapping("member/growthhistory")
public class GrowthHistoryController {
    @Autowired
    private GrowthHistoryService growthHistoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:growthhistory:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = growthHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:growthhistory:info")
    public R info(@PathVariable("id") Long id){
		GrowthHistoryEntity growthHistory = growthHistoryService.getById(id);

        return R.ok().put("growthHistory", growthHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:growthhistory:save")
    public R save(@RequestBody GrowthHistoryEntity growthHistory){
		growthHistoryService.save(growthHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:growthhistory:update")
    public R update(@RequestBody GrowthHistoryEntity growthHistory){
		growthHistoryService.updateById(growthHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:growthhistory:delete")
    public R delete(@RequestBody Long[] ids){
		growthHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
