package com.macro.xander.search.config;

import com.macro.xander.common.config.BaseSwaggerConfig;
import com.macro.xander.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/16 14:16
 * @email ：zhrunxin33@gmail.com
 * @description：Swagger相关配置（每个微服务都要）
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    protected SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.macro.xander.search.controller")
                .title("xander搜索系统")
                .description("xander搜索相关接口文档")
                .contactName("xander")
                .version("1.0")
                .enableSecurity(false)
                .build();
    }

    /**
     * 解决'documentationPluginsBootstrapper'报错
     */
    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }
}
