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



### 6.明确秒杀系统目标

1. 尽可能高的QPS/并发量
2. 防止商品超卖
3. 保证操作原子性（开启事务）

重点在于1，2两点。



### 7.防止商品超卖的方法

#### Ⅰ MySQL乐观锁 

```mysql
select version from goods WHERE id= 1001
-- 这种方式采用了版本号的方式，其实也就是 CAS 的原理。
-- 当然里面也有排它锁，但是MySQL在判断version不等后就会退出（因此这种方法是乐观锁）
update goods set num = num - 1, version = version + 1 WHERE id= 1001 AND num > 0 AND version = @version(上面查到的version);
```



#### Ⅱ MySQL悲观锁

```mysql
-- 这里使用了写锁（排它锁），是悲观锁的一种
update goods set num = num - 1 WHERE id = 1001 and num > 0
```

#### Ⅲ Redis单线程扣库存（配合lua）

在秒杀的情况下，高频率的去读写数据库，会严重造成性能问题。所以必须借助其他服务， 利用 redis 的单线程预减库存。比如商品有 100 件。那么我在 redis 存储一个 k,v。例如

每一个用户线程进来，key 值就减 1，等减到 0 的时候，全部拒绝剩下的请求。

那么也就是只有 100 个线程会进入到后续操作。所以一定不会出现超卖的现象。

#### Ⅳ 分布式锁

复杂，不推荐

#### 结论：使用Redis预减库存，配合MySQL悲观锁实现



## ① 最简单的实现example01（没有超卖,QPS低）

防止超卖方式：方案Ⅱ（MySQL悲观锁）

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



## ② 优化实现example02（隐藏接口，QPS较高）

防止超卖方案：**使用Redis预减库存，配合MySQL悲观锁实现**

```shell
# 思路（校验和秒杀接口分离）
# 1.校验
①查询限时购是否在时间范围内开启
②如果redis中没有该限时购库存，初始化进Redis
③根据用户ID和限时购ID和场次ID生成校验路径

# 2.秒杀（默认进来的请求已经校验正常）
①验证path路径
②查询Redis该秒杀是否结束了 Key：xander:seckill:end:promotionId_sessionId
③查询Redis是否已经秒杀超过限购数量 Key：xander:seckill:end:promotionId_sessionId Value：数量（lua脚本）
④预减Redis的库存（限时购表和商品表）
⑤减库存，生成订单下单（开事务）
```

#### 压测结果

没有出现超卖的情况，但是QPS变成了110，提升了接近4倍！！！

![image-20230315175715303](..\doc\resource\image-20230315175715303.png)
