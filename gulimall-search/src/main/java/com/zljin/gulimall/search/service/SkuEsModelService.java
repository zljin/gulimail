package com.zljin.gulimall.search.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.zljin.gulimall.common.to.es.SkuEsModel;
import com.zljin.gulimall.search.dao.SkuEsModelDao;
import com.zljin.gulimall.search.model.SkuEsDo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SkuEsModelService {

    @Resource
    SkuEsModelDao skuEsModelDao;

    public boolean productStatusUp(List<SkuEsModel> skuEsModels){
        if(CollectionUtil.isNotEmpty(skuEsModels)){
            skuEsModels.forEach(m ->{
                SkuEsDo skuEsDo = new SkuEsDo();
                BeanUtil.copyProperties(m,skuEsDo);
                skuEsModelDao.save(skuEsDo);
            } );
            return true;
        }
        return false;
    }
}
