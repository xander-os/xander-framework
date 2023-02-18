package com.macro.xander;

import net.hasor.spring.boot.EnableHasor;
import net.hasor.spring.boot.EnableHasorWeb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/17 20:40
 * @email ：zhrunxin33@gmail.com
 * @description：Hasor引擎无代码实现
 */
@EnableDiscoveryClient
@EnableHasor()    // 在Spring 中启用 Hasor
@EnableHasorWeb() // 将 hasor-web 配置到 Spring 环境中，Dataway 的 UI 是通过 hasor-web 提供服务。
@SpringBootApplication
public class XanderHasorApplication {
    public static void main(String[] args) {
        SpringApplication.run(XanderHasorApplication.class, args);
    }
}
