package com.zljin.gulimall.search.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.zljin.gulimall.common.to.es.SkuEsModel;
import com.zljin.gulimall.search.dao.SkuEsModelDao;
import com.zljin.gulimall.search.model.SkuEsDo;
import com.zljin.gulimall.search.vo.SearchParam;
import com.zljin.gulimall.search.vo.SearchResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    public SearchResult search(SearchParam param) {
        //1、动态构建出查询需要的DSL语句
        SearchResult result = new SearchResult();
        List<SkuEsModel> skuEsModels = new ArrayList<>();
//        Iterable<SkuEsDo> skuEsDos = skuEsModelDao.customComplexQuery(param.getKeyword(), param.getCatalog3Id(), param.getBrandId().get(0));
        Iterable<SkuEsDo> skuEsDos = skuEsModelDao.findAll();
        skuEsDos.forEach(m -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtil.copyProperties(m, skuEsModel);
            skuEsModels.add(skuEsModel);
        });
        result.setProducts(skuEsModels);
        return result;
    }
}
