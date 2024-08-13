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

import com.zljin.gulimall.member.entity.IntegrationHistoryEntity;
import com.zljin.gulimall.member.service.IntegrationHistoryService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.R;



/**
 * 购物积分记录表
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-13 09:14:36
 */
@RestController
@RequestMapping("member/integrationhistory")
public class IntegrationHistoryController {
    @Autowired
    private IntegrationHistoryService integrationHistoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:integrationhistory:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = integrationHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:integrationhistory:info")
    public R info(@PathVariable("id") Long id){
		IntegrationHistoryEntity integrationHistory = integrationHistoryService.getById(id);

        return R.ok().put("integrationHistory", integrationHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:integrationhistory:save")
    public R save(@RequestBody IntegrationHistoryEntity integrationHistory){
		integrationHistoryService.save(integrationHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:integrationhistory:update")
    public R update(@RequestBody IntegrationHistoryEntity integrationHistory){
		integrationHistoryService.updateById(integrationHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:integrationhistory:delete")
    public R delete(@RequestBody Long[] ids){
		integrationHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
