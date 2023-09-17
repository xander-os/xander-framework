# Sharing Nacos

## 一、简介

Nacos（**Dynamic Naming and Configuration Service**）是一个开源的分布式系统，用于服务发现、动态配置和服务路由。

主要功能：

**服务发现、动态配置、服务路由和流量管理**。

## 二、历史和背景

Nacos 在阿里巴巴起源于 2008 年五彩石项目，该项目完成了微服务拆分和业务中建设。

![img](https://oscimg.oschina.net/oscnet/up-1f89601337260b606c2ac77cf62a15158f6.JPEG)

![image.png](https://nacos.io/img/blog/2_2_0-release/220-roadmap.png)

![image.png](https://cdn.nlark.com/yuque/0/2022/png/1577777/1660125280551-a2e881fe-d25e-4ebb-a28f-8e56683deef1.png)

## 三、主要功能

- **服务发现和服务健康监测**

Nacos 支持基于 **DNS** 和基于 **RPC** 的服务发现。服务提供者使用 **原生SDK**、**OpenAPI**。

Nacos 提供对服务的实时的健康检查，阻止向不健康的主机或服务实例发送请求。Nacos 支持传输层 (PING 或 TCP)和应用层 (如 HTTP、MySQL、用户自定义）的健康检查。

- **动态配置服务**

动态配置服务可以让您以中心化、外部化和动态化的方式管理所有环境的应用配置和服务配置。

Nacos 还提供包括配置版本跟踪、金丝雀发布、一键回滚配置以及客户端配置更新状态跟踪在内的一系列开箱即用的配置管理特性，帮助您更安全地在生产环境中管理配置变更和降低配置变更带来的风险。

// TODO 动态配置使用@RefreshScope实现

- **动态 DNS 服务** Same as Eureka 

动态 DNS 服务支持权重路由，让您更容易地实现中间层负载均衡、更灵活的路由策略、流量控制以及数据中心内网的简单DNS解析服务。动态DNS服务还能让您更容易地实现以 DNS 协议为基础的服务发现，以帮助您消除耦合到厂商私有服务发现 API 上的风险。

- **服务及其元数据管理**

Nacos 能让您从微服务平台建设的视角管理数据中心的所有服务及元数据，包括管理服务的描述、生命周期、服务的静态依赖分析、服务的健康状态、服务的流量管理、路由及安全策略、服务的 SLA 以及最首要的 metrics 统计数据。

## 四、Why nacos?

- 高可用性和容错性
- 动态配置和服务发现的一体化解决方案。
- 强大的Web控制台和API支持。
- 活跃的社区，Apeche年度顶级开源项目。
- Who is using? https://github.com/alibaba/nacos#who-is-using
- In **Apeche Apisix**（Nginx + lua）: 用Nacos作为注册发现

SDK（Java、C、Go、Python...） way：https://nacos.io/zh-cn/docs/sdk.html

HTTP way：https://nacos.io/zh-cn/docs/open-api.html



Nacos releases: https://github.com/alibaba/nacos/releases

Eureka releases: https://github.com/Netflix/eureka/releases Only released a fix bug version in 2023



## 五、Concept

- 命名空间：用于隔离不同环境的配置和服务。
- 服务：代表一个微服务。
- 实例：代表一个微服务的运行实例。
- 配置：动态配置的管理单元。
- 元信息：Nacos数据（如配置和服务）描述信息，如服务版本、权重、容灾策略、负载均衡策略、鉴权配置、各种自定义标签 (label)，从作用范围来看，分为服务级别的元信息、集群的元信息及实例的元信息。
- Nacos的架构：包括Server端、Client端和Web控制台。



## 六、Demo

Standard demo：http://console.nacos.io/nacos/index.html#/configurationManagement?dataId=&group=&appName=&namespace=&pageSize=&pageNo=

Project demo：http://150.158.155.246:8848/nacos/#/configurationManagement?dataId=&group=&appName=&namespace=&pageSize=&pageNo=



## 七：Extend

**短链接 -> 长连接（1.0） -> 使用gRPC调用(2.0)**

配置监听中使用长轮询减少不必要的轮询请求，降低服务器和网络的负担，同时提供了近实时的通知功能。

长连接有什么问题？

而服务注册中心的推送则通过UDP推送+HTTP定期对账来实现。然而，配置中心的长轮训、服务注册中心的定期对账，都需要周期性地对于服务端进行一次主动建连和配置传送，增大服务端的内存开销；随着Nacos用户的服务数和配置数规模的增大，服务端的内存泄漏风险也大大增加。

为什么gRPC能实现？

**双向通讯模型**和**流式 RPC**

```java
@PostMapping("/listener")
public void listener(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
    String probeModify = request.getParameter("Listening-Configs");
    probeModify = URLDecoder.decode(probeModify, Constants.ENCODE);
    
    Map<String, String> clientMd5Map;
        clientMd5Map = MD5Util.getClientMd5Map(probeModify);
    
    // do long-polling
    inner.doPollingConfig(request, response, clientMd5Map, probeModify.length());
}
```



```java
public void addLongPollingClient(HttpServletRequest req, HttpServletResponse rsp, Map<String, String> clientMd5Map,
            int probeRequestSize) {
        
        String str = req.getHeader(LongPollingService.LONG_POLLING_HEADER);
        String noHangUpFlag = req.getHeader(LongPollingService.LONG_POLLING_NO_HANG_UP_HEADER);
        int delayTime = SwitchService.getSwitchInteger(SwitchService.FIXED_DELAY_TIME, 500);
        
        // Add delay time for LoadBalance, and one response is returned 500 ms in advance to avoid client timeout.
        long timeout = -1L;
        if (isFixedPolling()) {
            timeout = Math.max(10000, getFixedPollingInterval());
            // Do nothing but set fix polling timeout.
        } else {
            timeout = Math.max(10000, Long.parseLong(str) - delayTime);
            long start = System.currentTimeMillis();
            List<String> changedGroups = MD5Util.compareMd5(req, rsp, clientMd5Map);
            if (changedGroups.size() > 0) {
                generateResponse(req, rsp, changedGroups);
                LogUtil.CLIENT_LOG.info("{}|{}|{}|{}|{}|{}|{}", System.currentTimeMillis() - start, "instant",
                        RequestUtil.getRemoteIp(req), "polling", clientMd5Map.size(), probeRequestSize,
                        changedGroups.size());
                return;
            } else if (noHangUpFlag != null && noHangUpFlag.equalsIgnoreCase(TRUE_STR)) {
                LogUtil.CLIENT_LOG.info("{}|{}|{}|{}|{}|{}|{}", System.currentTimeMillis() - start, "nohangup",
                        RequestUtil.getRemoteIp(req), "polling", clientMd5Map.size(), probeRequestSize,
                        changedGroups.size());
                return;
            }
        }
        String ip = RequestUtil.getRemoteIp(req);
        ConnectionCheckResponse connectionCheckResponse = checkLimit(req); // 检测相同ip，appName下是否已经有长轮询
        if (!connectionCheckResponse.isSuccess()) {
            generate503Response(req, rsp, connectionCheckResponse.getMessage());
            return;
        }
        
        // Must be called by http thread, or send response.
        final AsyncContext asyncContext = req.startAsync(); // 开启异步，Servlet3.0 new feature
        
        // AsyncContext.setTimeout() is incorrect, Control by oneself
        asyncContext.setTimeout(0L);
        
        String appName = req.getHeader(RequestUtil.CLIENT_APPNAME_HEADER);
        String tag = req.getHeader("Vipserver-Tag");
        ConfigExecutor.executeLongPolling(
                new ClientLongPolling(asyncContext, clientMd5Map, ip, probeRequestSize, timeout, appName, tag));
    }
```



```java
        @Override
        public void run() {
            asyncTimeoutFuture = ConfigExecutor.scheduleLongPolling(() -> {
                try {
                    getRetainIps().put(ClientLongPolling.this.ip, System.currentTimeMillis());
                    
                    // Delete subscriber's relations.
                    boolean removeFlag = allSubs.remove(ClientLongPolling.this);
                    
                    if (removeFlag) {
                        if (isFixedPolling()) {
                            LogUtil.CLIENT_LOG
                                    .info("{}|{}|{}|{}|{}|{}", (System.currentTimeMillis() - createTime), "fix",
                                            RequestUtil.getRemoteIp((HttpServletRequest) asyncContext.getRequest()),
                                            "polling", clientMd5Map.size(), probeRequestSize);
                            List<String> changedGroups = MD5Util
                                    .compareMd5((HttpServletRequest) asyncContext.getRequest(),
                                            (HttpServletResponse) asyncContext.getResponse(), clientMd5Map);
                            if (changedGroups.size() > 0) {
                                sendResponse(changedGroups);
                            } else {
                                sendResponse(null);
                            }
                        } else {
                            LogUtil.CLIENT_LOG
                                    .info("{}|{}|{}|{}|{}|{}", (System.currentTimeMillis() - createTime), "timeout",
                                            RequestUtil.getRemoteIp((HttpServletRequest) asyncContext.getRequest()),
                                            "polling", clientMd5Map.size(), probeRequestSize);
                            sendResponse(null);
                        }
                    } else {
                        LogUtil.DEFAULT_LOG.warn("client subsciber's relations delete fail.");
                    }
                } catch (Throwable t) {
                    LogUtil.DEFAULT_LOG.error("long polling error:" + t.getMessage(), t.getCause());
                }
                
            }, timeoutTime, TimeUnit.MILLISECONDS);
            
            allSubs.add(this);
        }
        
        void sendResponse(List<String> changedGroups) {
            
            // Cancel time out task.
            if (null != asyncTimeoutFuture) {
                asyncTimeoutFuture.cancel(false); 
            }
            generateResponse(changedGroups); // End the request if changedGroups is not null
        }
```

