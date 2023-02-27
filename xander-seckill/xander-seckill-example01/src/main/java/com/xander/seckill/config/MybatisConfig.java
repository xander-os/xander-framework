package com.xander.seckill.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/23 21:17
 * @email ：zhrunxin33@gmail.com
 * @description：MyBatis配置
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.xander.seckill.dao","com.xander.seckill.mapper","com.macro.xander.dao","com.macro.xander.mapper"})
public class MybatisConfig {
}
