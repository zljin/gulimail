package com.zljin.gulimall.zipkin;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZipkinServer
public class GulimallZipkinApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(GulimallZipkinApplication.class)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}

}
