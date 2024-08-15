package com.zljin.gulimall.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zljin.gulimall.product.service.CategoryBrandRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        return categoryEntities.stream().filter(t -> t.getParentCid() == 0)
                .map(t -> {
                    t.setChildren(getChildren(t, categoryEntities));
                    return t;
                }).collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> ids) {
        boolean flag = true;
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        for (int i = 0; i < ids.size(); i++) {
            Long id = ids.get(i);
            CategoryEntity categoryEntity = baseMapper.selectById(id);
            if (CollectionUtil.isNotEmpty(getChildren(categoryEntity, categoryEntities))) {
                flag = false;
                log.info("被删除的菜单，又被其他地方引用，无法删除");
                break;
            }
        }
        if (flag) {
            baseMapper.deleteBatchIds(ids);
        }
    }

    /**
     * 找到catelogId的完整路径
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> path = new LinkedList<>();
        findPath(catelogId, path);
        Collections.reverse(path);
        return path.toArray(new Long[path.size()]);
    }

    /**
     * 级联更新
     * @param category
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    private void findPath(Long categorygId, List<Long> path) {
        if (categorygId != 0) {
            path.add(categorygId);
            CategoryEntity byId = getById(categorygId);
            findPath(byId.getParentCid(), path);
        }
    }

    private List<CategoryEntity> getChildren(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntities) {
        return categoryEntities.stream().filter(t -> t.getParentCid().equals(categoryEntity.getCatId())).map(t -> {
            t.setChildren(getChildren(t, categoryEntities));
            return t;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
    }

}