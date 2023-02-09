package com.macro.xander.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/9 21:17
 * @email ：zhrunxin33@gmail.com
 * @description：MyBatis配置
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.macro.xander.dao","con.macro.xander.mapper"})
public class MybatisConfig {
}
