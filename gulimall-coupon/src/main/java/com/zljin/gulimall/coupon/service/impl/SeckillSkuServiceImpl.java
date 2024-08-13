package com.zljin.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.Query;

import com.zljin.gulimall.coupon.dao.SeckillSkuDao;
import com.zljin.gulimall.coupon.entity.SeckillSkuEntity;
import com.zljin.gulimall.coupon.service.SeckillSkuService;


@Service("seckillSkuService")
public class SeckillSkuServiceImpl extends ServiceImpl<SeckillSkuDao, SeckillSkuEntity> implements SeckillSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSkuEntity> page = this.page(
                new Query<SeckillSkuEntity>().getPage(params),
                new QueryWrapper<SeckillSkuEntity>()
        );

        return new PageUtils(page);
    }

}