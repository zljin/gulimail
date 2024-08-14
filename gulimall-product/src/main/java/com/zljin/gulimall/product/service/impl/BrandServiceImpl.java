package com.zljin.gulimall.product.service.impl;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.Query;

import com.zljin.gulimall.product.dao.BrandDao;
import com.zljin.gulimall.product.entity.BrandEntity;
import com.zljin.gulimall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    /**
     * 附带条件的分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StrUtil.isNotEmpty(key)) {
            queryWrapper.eq("brand_id", key).or().like("name", key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

}