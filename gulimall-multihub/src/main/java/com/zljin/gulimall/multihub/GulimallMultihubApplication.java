package com.zljin.gulimall.multihub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GulimallMultihubApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMultihubApplication.class, args);
    }

}
