package com.macro.xander.config;

import com.macro.xander.common.config.BaseRedisConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 22:16
 * @email ：zhrunxin33@gmail.com
 * @description：Redis配置，把Redis交给容器管理操作放在具体模块
 */
@Configuration
public class RedisConfig extends BaseRedisConfig {
}
