package com.macro.xander;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 21:35
 * @email ：zhrunxin33@gmail.com
 * @description：网关微服务
 */
@EnableDiscoveryClient
@SpringBootApplication
public class XanderGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(XanderGatewayApplication.class, args);
    }
}
