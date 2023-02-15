package com.macro.xander;


import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/15 14:47
 * @email ：zhrunxin33@gmail.com
 * @description：监控微服务启动类
 */
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
public class XanderMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(XanderMonitorApplication.class, args);
    }
}
