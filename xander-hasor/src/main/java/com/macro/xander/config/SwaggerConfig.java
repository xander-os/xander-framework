package com.macro.xander.config;

import com.macro.xander.common.config.BaseSwaggerConfig;
import com.macro.xander.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/17 21:41
 * @email ：zhrunxin33@gmail.com
 * @description：Swagger子模块自定义配置
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig extends BaseSwaggerConfig {
    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.macro.xander.controller")
                .title("xander无代码平台")
                .description("xander无代码平台相关接口文档")
                .contactName("xander")
                .version("1.0")
                .enableSecurity(true)
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
