package com.zljin.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.Query;

import com.zljin.gulimall.product.dao.CategoryDao;
import com.zljin.gulimall.product.entity.CategoryEntity;
import com.zljin.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        return categoryEntities.stream().filter(t->t.getParentId()==0)
                .map(t->{
                    t.setChildren(getChildren(t,categoryEntities));
                    return t;
                }).collect(Collectors.toList());
    }

    private List<CategoryEntity> getChildren(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntities) {
        return categoryEntities.stream().filter(t -> t.getParentId().equals(categoryEntity.getId())).map(t -> {
            t.setChildren(getChildren(t, categoryEntities));
            return t;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
    }

}