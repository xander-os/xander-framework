package com.macro.xander.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/3 9:36
 * @email ：zhrunxin33@gmail.com
 * @description：校验微服务启动类
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.macro.xander")
public class XanderAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(XanderAuthApplication.class, args);
    }
}
