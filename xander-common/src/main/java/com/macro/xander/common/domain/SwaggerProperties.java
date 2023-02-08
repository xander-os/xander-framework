package com.macro.xander.common.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/4 16:56
 * @email ：zhrunxin33@gmail.com
 * @description：Swagger自定义配置属性对象
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class SwaggerProperties {
    /**
     * API文档生成基础路径
     */
    private String apiBasePackage;
    /**
     * 是否要启用登录认证
     */
    private boolean enableSecurity;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档描述
     */
    private String description;
    /**
     * 文档版本
     */
    private String version;
    /**
     * 文档联系人姓名
     */
    private String contactName;
    /**
     * 文档联系人网址
     */
    private String contactUrl;
    /**
     * 文档联系人邮箱
     */
    private String contactEmail;
}
