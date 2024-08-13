package com.zljin.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.Query;

import com.zljin.gulimall.coupon.dao.CouponSpuCategoryDao;
import com.zljin.gulimall.coupon.entity.CouponSpuCategoryEntity;
import com.zljin.gulimall.coupon.service.CouponSpuCategoryService;


@Service("couponSpuCategoryService")
public class CouponSpuCategoryServiceImpl extends ServiceImpl<CouponSpuCategoryDao, CouponSpuCategoryEntity> implements CouponSpuCategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CouponSpuCategoryEntity> page = this.page(
                new Query<CouponSpuCategoryEntity>().getPage(params),
                new QueryWrapper<CouponSpuCategoryEntity>()
        );

        return new PageUtils(page);
    }

}