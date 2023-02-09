package com.macro.xander;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/9 0:41
 * @email ：zhrunxin33@gmail.com
 * @description：Admin服务入口
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class XanderAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(XanderAdminApplication.class, args);
    }
}
