package com.zljin.gulimall.product.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.Query;

import com.zljin.gulimall.product.dao.AttrGroupDao;
import com.zljin.gulimall.product.entity.AttrGroupEntity;
import com.zljin.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * select * from pms_attr_group where catelog_id=?
     * and (attr_group_id=key or attr_group_name like %key%)
     * @param params
     *
     * {
     *     "page":"1", //当前页码
     *     "limit":"10", //每页记录数
     *     "sidx":'id', //排序字段
     *     "order":"desc",//排序方式
     *     "key":"huawei"//检索关键字
     *
     * }
     *
     * @param catelogId
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, long catelogId) {
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = MapUtil.getStr(params,"key","");
        if (StrUtil.isNotEmpty(key)){
            queryWrapper.and((obj)->
                    obj.eq("attr_group_id",key).or().like("attr_group_name",key));
        }
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

}