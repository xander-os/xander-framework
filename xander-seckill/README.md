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



### 3.生成测试数据

:star:生成结果：当i为1001时

- 生成数据为id=1001,username=member1001
- 对应收获地址id为10000+1001=11001

```mysql
delimiter $$     -- 以delimiter来标记用$表示存储过程结束
create procedure generate()		-- 创建生成数据方法
begin
declare i int;		-- 定义i变量
set i=1001;
while i<5000 do		-- 对i的值配置
	-- 插入member表(默认密码123456)
	INSERT INTO `xander`.`ums_member`(`id`, `member_level_id`, `username`, `password`, `nickname`, `phone`, `status`, `create_time`) VALUES (i, 4, CONCAT('member',i), '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', CONCAT('member',i), CONCAT('1706158',i), 1, now());
	-- 插入收货地址表
	INSERT INTO `xander`.`ums_member_receive_address`(`id`, `member_id`, `name`, `phone_number`, `default_status`, `post_code`, `province`, `city`, `region`, `detail_address`) VALUES (10000 + i, i, concat('小周',i,'号'), CONCAT('1706158',i), 1, '518000', '广东省', '深圳市', '福田区', concat('东晓街道小周之家',i,'号'));
set i=i+1;		-- 自增循环
end while;
end 
$$		-- 存储过程结束
 
call generate();		-- 调用生成方法
```

数据库时区修改为东八区

:star:注意需要修改数据库配置文件才是永久的

修改配置文件 /etc/my.cnf

```csharp
[mysqld]
default-time_zone = '+8:00'
```



```mysql
# 查询当前设置
select @@GLOBAL.time_zone,@@SESSION.time_zone
show variables like "%time_zone%";
-- 设置全局
set global time_zone = '+8:00';
set time_zone='+8:00';
-- 立即生效
flush privileges; 
select now();
```



### 4.生成JMeter参数文件（包含Token，限时购等请求参数）

xander-seckill-example01模块的CreateTokenTxt测试类可以在当前模块根目录生成token.txt



### 4.生成JMeter脚本

![image-20230301115045572](..\doc\resource\image-20230301115045572.png)

可以导入我提供的jmx文件



### 5.空接口测试最大QPS

我这里最大QPS为1000

![image-20230301121128073](..\doc\resource\image-20230301121128073.png)

## ① 最简单的实现example01（没有超卖,QPS低）

```shell
# 业务流程
①查询限时购表是否在时间范围内开启
②查询该场次库存是否足够
③是否Redis标志已经结束
④从限时购反查询商品表和库存表
⑤减库存，下单
```

#### 压测结果

没有出现超卖的情况，但是QPS只有30.0/sec

![image-20230301122205041](..\doc\resource\image-20230301122205041.png)
