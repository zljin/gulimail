package com.zljin.gulimall.search.dao;

import com.zljin.gulimall.search.model.SkuEsDo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface SkuEsModelDao extends ElasticsearchRepository<SkuEsDo, Long> {

}
