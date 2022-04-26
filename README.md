# Fantasy

## Fantasy-Base

基础服务包，公共类、实体类、Feign类、基类、服务配置都定义于此。

- banner.txt springboot启动打印文本
- bootstrap-dev/product.properties 注册中心、配置中心配置
- feign-service.properties 检测本地服务是否开启并直连联调配置
- static 静态文件目录，存放公共css、js
- 演示环境请在bootstrap-dev.properties中配置如下设置：

```properties
spring.cloud.nacos.discovery.server-addr=http://120.78.148.113:8848
spring.cloud.nacos.discovery.namespace=2abf2a56-8efa-4045-b719-8d26b1e4a281
spring.cloud.nacos.config.server-addr=http://120.78.148.113:8848
spring.cloud.nacos.config.namespace=2abf2a56-8efa-4045-b719-8d26b1e4a281
spring.cloud.nacos.discovery.register-enabled=false
spring.cloud.nacos.username=fantasy-gitee
spring.cloud.nacos.password=G3CXv$kaq9KVuyZz6h
```

或者IDEA中启动配置Environment中VM Options增加以下配置

```properties
-Dspring.cloud.nacos.username=fantasy-gitee
-Dspring.cloud.nacos.password=G3CXv$kaq9KVuyZz6h
-Dspring.cloud.nacos.discovery.server-addr=http://120.78.148.113:8848
-Dspring.cloud.nacos.discovery.namespace=2abf2a56-8efa-4045-b719-8d26b1e4a281
-Dspring.cloud.nacos.config.server-addr=http://120.78.148.113:8848
-Dspring.cloud.nacos.config.namespace=2abf2a56-8efa-4045-b719-8d26b1e4a281
-Dspring.cloud.nacos.discovery.register-enabled=false
```

## Fantasy-Gateway

服务网关，实现了knife swagger文档聚合，网关检测本地服务启动直调。

## Fantasy-Gateway

授权中心，，主要功能如下：

- oauth2登录方式，目前有密码登录和邮箱登录方式
- 用户
- 角色
- 客户端

## Fantasy-Chess

棋牌室计分，主要功能如下：

- 创建棋牌房
- 棋牌房内分数结算
- 棋牌房历史记录

通过websocket发送房间信息，未实现websocket的集群。

## Fantasy-Oss

工具与存储，主要功能如下：

- MinIo上传存储
- 待办邮箱提醒
- 工具等