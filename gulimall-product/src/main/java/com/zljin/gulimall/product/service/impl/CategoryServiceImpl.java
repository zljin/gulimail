package com.zljin.gulimall.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.Query;
import com.zljin.gulimall.product.dao.CategoryDao;
import com.zljin.gulimall.product.entity.CategoryEntity;
import com.zljin.gulimall.product.service.CategoryBrandRelationService;
import com.zljin.gulimall.product.service.CategoryService;
import com.zljin.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    /**
     * 1、空结果缓存：解决缓存穿透
     * 2、设置过期时间（加随机值）：解决缓存雪崩
     * 3、加锁：解决缓存击穿
     * @return
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if(StrUtil.isNotEmpty(catalogJSON)){
            return JSONUtil.toBean(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {}, true);
        }
        return getCatalogJsonFromDbWithRedisLock();
    }

    /**
     * redis setnx 分布式加锁
     * @return
     */
    private Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock() {
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            //加锁操作
            Map<String, List<Catelog2Vo>> dataFromDb;
            try {
                dataFromDb = getDataFromDb();
                stringRedisTemplate.opsForValue().set("catalogJSON", JSONUtil.toJsonStr(dataFromDb), 1, TimeUnit.DAYS);
            } finally {
                //释放锁
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                Long lock1 = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class)
                        , Arrays.asList("lock"), uuid);
            }
            return dataFromDb;
        }
        //如果加锁失败，自旋的方式休眠200ms重试
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        return getCatalogJsonFromDbWithRedisLock();//自旋的方式
    }

    private Map<String, List<Catelog2Vo>> getDataFromDb(){
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = getParentCid(selectList, 0L);
        //2、封装数据
        return level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1、每一个的一级分类，查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParentCid(selectList, v.getCatId());
            //2、封装上面面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1、找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog = getParentCid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            //2、封装成指定格式
                            return new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
    }

    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList, Long parent_cid) {
        return selectList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
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