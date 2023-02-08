package com.macro.xander.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/8 10:13
 * @email ：zhrunxin33@gmail.com
 * @description：网关白名单配置
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "secure.ignore")
public class IgnoreUrlsConfig {
    private List<String> urls;
}
