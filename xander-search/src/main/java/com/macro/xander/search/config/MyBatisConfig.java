package com.macro.xander.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/16 14:13
 * @email ：zhrunxin33@gmail.com
 * @description：MyBatis相关配置
 */
@Configuration
@MapperScan({"com.macro.xander.mapper","com.macro.xander.search.dao"})
public class MyBatisConfig {
}
