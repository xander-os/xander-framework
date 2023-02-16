package com.macro.xander.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/16 11:54
 * @email ：zhrunxin33@gmail.com
 * @description：Es搜索微服务
 */
@EnableDiscoveryClient
@SpringBootApplication
public class XanderSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(XanderSearchApplication.class, args);
    }
}
