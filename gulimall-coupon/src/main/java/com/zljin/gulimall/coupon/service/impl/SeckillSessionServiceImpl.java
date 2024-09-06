package com.zljin.gulimall.coupon.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zljin.gulimall.coupon.entity.SeckillSkuEntity;
import com.zljin.gulimall.coupon.service.SeckillSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.Query;

import com.zljin.gulimall.coupon.dao.SeckillSessionDao;
import com.zljin.gulimall.coupon.entity.SeckillSessionEntity;
import com.zljin.gulimall.coupon.service.SeckillSessionService;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    @Autowired
    SeckillSkuService seckillSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSessionEntity> getLates3DaySession() {
        List<SeckillSessionEntity> list = this.list(new QueryWrapper<SeckillSessionEntity>().between("start_time", startTime(), endTime()));
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.stream().map(session -> {
            Long id = session.getId();
            session.setRelationSkus(seckillSkuService.list(new QueryWrapper<SeckillSkuEntity>().eq("promotion_session_id", id)));
            return session;
        }).collect(Collectors.toList());
    }

    private String startTime() {
        LocalDate now = LocalDate.now();
        LocalTime min = LocalTime.MIN;
        LocalDateTime start = LocalDateTime.of(now, min);
        return start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String endTime() {
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        LocalDateTime of = LocalDateTime.of(localDate, LocalTime.MAX);
        return of.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


}