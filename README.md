![logo](doc/resource/logo.png)
# 一套规范标准的微服务开发框架

[![license](http://img.shields.io/badge/license-MIT-green.svg)](https://github.com/King-Run/xander-framework)
[![JAVA](https://img.shields.io/badge/language-java-red.svg)](https://github.com/King-Run/xander-framework)
[![platform](https://img.shields.io/badge/platform-linux%20|%20macos%20|%20windows-blue.svg)](https://github.com/King-Run/xander-framework)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-yellow.svg)](https://github.com/King-Run/xander-framework)

## 介绍
使用当前最热门的SpringCloud项目技术，提升代码规范和对分布式理解。打造一套规范的微服务开发框架。

## 组织结构

``` lua
xander
├── xander-common -- 工具类及通用代码模块
├── xander-hasor -- 零代码开发服务
├── xander-mbg -- MyBatisGenerator生成的数据库操作代码模块
├── xander-executor -- XxlJob的执行器服务
├── xander-seckill -- 秒杀模块（涉及高并发）
├── xander-auth -- 基于Spring Security Oauth2的统一的认证中心
├── xander-gateway -- 基于Spring Cloud Gateway的微服务API网关服务
├── xander-xxljob -- XxlJob调度服务
├── xander-monitor -- 基于Spring Boot Admin的微服务监控中心
├── xander-admin -- 后台管理系统服务
├── xander-search -- 基于Elasticsearch的搜索系统服务
├── xander-portal -- 门户系统服务
├── xander-demo -- 微服务远程调用测试服务
└── config -- 配置中心存储的配置
```

## 重要版本更新说明

### 当前xander 5.0版本
- 秒杀模块Example01接入（重在技术学习）
- 业务功能齐全，支持单商品多数量秒杀
- 后续使用JMeter压测

### xander 1.0 版本

- 引入nacos模块作为注册中心和配置中心
- xander-common作为工具类及通用代码模块
- xander-gateway作为网关服务
- xander-auth负责权限校验


### xander 2.0 版本

- 新增xander-hasor，实现零代码开发服务
- 增加xander-admin模块和集成XxlJob远程调度
- 增加xander-search模块，集成ES（做好权限隔绝，ES模块不具备修改MySQL表的权限）


### xander 3.0 版本

- 新增xander-monitor，集成Spring-Boot-Admin服务。
- 对SpringBoot应用的各项指标进行监控，作为微服务架构中的监控中心


### xander 4.0 版本

- 集成XxlJob分布式调度系统以及执行器服务

## 后续版本说明
### xander 5.0 版本
- XxlJob模块优化
- 秒杀模块接入（重在技术学习）

## 技术选型

### 后端技术

| 技术                     | 说明            | 官网                                                   |
|------------------------|---------------|------------------------------------------------------|
| Spring Cloud           | 微服务框架         | https://spring.io/projects/spring-cloud              |
| Spring Cloud Alibaba   | 微服务框架         | https://github.com/alibaba/spring-cloud-alibaba      |
| Spring Boot            | 容器+MVC框架      | https://spring.io/projects/spring-boot               |
| Spring Security Oauth2 | 认证和授权框架       | https://spring.io/projects/spring-security-oauth     |
| MyBatis                | ORM框架         | http://www.mybatis.org/mybatis-3/zh/index.html       |
| Hasor                  | 无代码框架         | https://www.hasor.net/                               |
| XxlJob                 | 分布式调度系统       | https://www.xuxueli.com/xxl-job/       |
| MyBatisGenerator       | 数据层代码生成       | http://www.mybatis.org/generator/index.html          |
| PageHelper             | MyBatis物理分页插件 | http://git.oschina.net/free/Mybatis_PageHelper       |
| Knife4j                | 文档生产工具        | https://github.com/xiaoymin/swagger-bootstrap-ui     |
| Elasticsearch          | 搜索引擎          | https://github.com/elastic/elasticsearch             |
| RabbitMq               | 消息队列          | https://www.rabbitmq.com/                            |
| Redis                  | 分布式缓存         | https://redis.io/                                    |
| MongoDb                | NoSql数据库      | https://www.mongodb.com/                             |
| Docker                 | 应用容器引擎        | https://www.docker.com/                              |
| Druid                  | 数据库连接池        | https://github.com/alibaba/druid                     |
| OSS                    | 对象存储          | https://github.com/aliyun/aliyun-oss-java-sdk        |
| MinIO                  | 对象存储          | https://github.com/minio/minio                       |
| JWT                    | JWT登录支持       | https://github.com/jwtk/jjwt                         |
| LogStash               | 日志收集          | https://github.com/logstash/logstash-logback-encoder |
| Lombok                 | 简化对象封装工具      | https://github.com/rzwitserloot/lombok               |
| Seata                  | 全局事务管理框架      | https://github.com/seata/seata                       |
| Portainer              | 可视化Docker容器管理 | https://github.com/portainer/portainer               |
| Jenkins                | 自动化部署工具       | https://github.com/jenkinsci/jenkins                 |
| Kubernetes             | 应用容器管理平台      | https://kubernetes.io/                               |