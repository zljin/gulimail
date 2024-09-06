package com.zljin.gulimall.seckill.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * 异步
 * @EnableAsync 开启异步任务功能
 * @Async 给希望异步执行的方法上标注
 * 自动配置类 TaskExecutionAutoConfiguration 属性绑定在TaskExecutionProperties
 */
@Slf4j
@EnableAsync
@EnableScheduling
@Configuration
public class ScheduledConfig {

    /**
     * 定时任务不应该阻塞,该异步
     */
    @Async
    @Scheduled(cron = "* * * ? * 5")
    public void hello() throws InterruptedException {
        log.info("hello...");
        Thread.sleep(3000);
    }


}