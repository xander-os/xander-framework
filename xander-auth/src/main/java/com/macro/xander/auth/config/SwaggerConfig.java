package com.macro.xander.auth.config;

import com.macro.xander.common.config.BaseSwaggerConfig;
import com.macro.xander.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/6 20:06
 * @email ：zhrunxin33@gmail.com
 * @description：Swagger子模块API接口配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    /**
     * 子模块自定义配置
     */
    @Override
    protected SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.macro.xander.auth.controller")
                .title("xander认证中心")
                .description("xander认证中心接口文档")
                .contactName("xander")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }

    /**
     * 解决'documentationPluginsBootstrapper'报错
     */
    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor(){
        return generateBeanPostProcessor();
    }
}
