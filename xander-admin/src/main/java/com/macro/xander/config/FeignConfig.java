package com.macro.xander.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/13 23:00
 * @email ：zhrunxin33@gmail.com
 * @description：Feign配置
 */
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}