package com.zljin.gulimall.seckill.config;

import com.zljin.gulimall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 异步
 *
 * @EnableAsync 开启异步任务功能
 * @Async 给希望异步执行的方法上标注
 * 自动配置类 TaskExecutionAutoConfiguration 属性绑定在TaskExecutionProperties
 */
@Slf4j
@EnableAsync
@EnableScheduling
@Configuration
public class ScheduledConfig {


    @Resource
    SeckillService seckillService;

    @Autowired
    RedissonClient redissonClient;

    private final String upload_lock = "seckill:upload:lock";

    /**
     * 定时任务不应该阻塞,该异步
     */
//    @Async
//    @Scheduled(cron = "* * * ? * 5")
//    public void hello() throws InterruptedException {
//        log.info("hello...");
//        Thread.sleep(3000);
//    }

    /**
     * 每分钟执行一次商品上架功能
     *
     * 记得留意一下幂等性
     */
    @Scheduled(cron = "0 * * * * ?")
    public void uploadSeckillSkuLatest3Days() {
        log.info("上架秒杀的商品信息...");
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillService.uploadSeckillSkuLatest3Days();
        } finally {
            lock.unlock();
        }
    }


}