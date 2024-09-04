package com.zljin.gulimall.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Leonard
 * Feign远程调用丢失请求头信息，即购物车就会拿不到该客户的session信息
 * 我们只要定制化Feign,将头信息加进去即可
 */
@Slf4j
@Configuration
public class GuliFeignConfig {

    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor(){
            @Override
            public void apply(RequestTemplate template) {
                //1、RequestContextHolder拿到刚进来的这个请求
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if(attributes!=null){
                    log.info("RequestInterceptor线程....{}",Thread.currentThread().getId());
                    HttpServletRequest request = attributes.getRequest(); //老请求
                    if(request != null){
                        //同步请求头数据，Cookie
                        String cookie = request.getHeader("Cookie");
                        //给新请求同步了老请求的cookie
                        template.header("Cookie",cookie);
                    }
                }
            }
        };
    }
}
