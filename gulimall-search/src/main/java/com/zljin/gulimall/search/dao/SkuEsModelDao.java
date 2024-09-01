package com.zljin.gulimall.search.dao;

import com.zljin.gulimall.search.model.SkuEsDo;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface SkuEsModelDao extends ElasticsearchRepository<SkuEsDo, Long> {
    @Query("{\"bool\":{\"must\":[{\"match\":{\"skuTitle\":{\"query\":?0}}}],\"filter\":[{\"term\":{\"catalogId\":?1}},{\"terms\":{\"brandId\":?2}}]}}")
    Iterable<SkuEsDo> customComplexQuery(String skuTitle,Long catalogId,Long brandId);
}
