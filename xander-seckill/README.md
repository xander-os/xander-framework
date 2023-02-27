#带你一步步玩转高并发

## 准备
前言：该教程基于GitHub上的

[秒杀]: https://github.com/qiurunze123/miaosha	"秒杀"



### 1.JMeter安装

没有JMeter压力测试，可能永远也不知道为什么需要高并发，为什么不安全

```shell
#win用户安装.zip的二进制安装包
https://jmeter.apache.org/download_jmeter.cgi
```


从不安全实例到承受高并发不崩溃的全示例



### 2.数据结构创建

```
导入SQL文件
```

具体表用途

``` lua
SQL
├── sms_flash_promotion_session -- 限时购场次表（定时间）
├── sms_flash_promotion -- 限时购表（一个秒杀项目）
├── sms_flash_promotion_product_relation -- 商品限时购与商品关系表
├── pms_product -- 商品表
├── pms_sku_stock -- sku的库存（锁库存，减库存）
├── oms_order -- 订单表
├── oms_order_item -- 订单中所包含的商品（秒杀一般只会一件商品）
```





## ① 最简单的实现（没有事务，没有锁）

```shell
# 业务流程
①查询商品库存是否大于0，小于0就把结束标志写到Redis
①减库存
②创建订单
③设置该商品已抢
```

