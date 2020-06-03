package com.oliver.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableDiscoveryClient
@Configuration
@EnableFeignClients
/**
 * @Author: cpeter
 * @Desc: 启动类
 * @Date: cretead in 2019/10/17 18:05
 * @Last Modified: by
 * @return value
 */
public class NettyApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NettyApplication.class);
    }
    public static void main(String[] args){
        SpringApplication.run(NettyApplication.class,args);
    }
}
