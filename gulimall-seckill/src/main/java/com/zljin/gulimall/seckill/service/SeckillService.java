package com.zljin.gulimall.seckill.service;

import com.zljin.gulimall.seckill.to.SecKillSkuRedisTo;

import java.util.List;

public interface SeckillService {

    /**
     * 秒杀商品上架
     */
    void uploadSeckillSkuLatest3Days();

    List<SecKillSkuRedisTo> getCurrentSeckillSkus();
}
