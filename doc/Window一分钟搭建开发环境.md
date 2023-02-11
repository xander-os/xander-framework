# 一分钟搭建运行开发环境



### 1.ELK相关配置

确保logstash.conf已被放进docker\docker_volumes\logstash目录下



### 2.执行

命令行切换到doc/docker目录下

```powershell
docker-compose -f docker-compose-env-win.yml up -d
```



### 3.扩展Logstash功能

```shell
# 进入容器使用如下命令安装插件
logstash-plugin install logstash-codec-json_lines
# 等待出现Installation successful就表示成功
```



相关

Nacos：http://127.0.0.1:8848/nacos/ 初始账号密码nacos

MySQL：