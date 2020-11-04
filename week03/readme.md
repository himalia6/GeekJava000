极客时间Java训练营第三周作业
=========================

#### 1. 实现完善gateway的基本功能
* [x] 简单路由 [HttpRouterHandler.java](./src/main/java/gw/netty/router/HttpRouterHandler.java)
* [x] 负载均衡 [LoadBalancerHandler.java](./src/main/java/gw/netty/balancer/LoadBalancerHandler.java)
    * [x] Random算法 [RandomBalancer.java](./src/main/java/gw/netty/balancer/RandomBalancer.java)
    * [x] RoundRobin算法 [RoundRobinBalancer.java](./src/main/java/gw/netty/balancer/RoundRobinBalancer.java)
    * [ ] 加权算法
* [ ] 基于Netty的带线程池的HttpClient


#### 2. Handlers Dataflow

在[HttpChannelInitializer.java](./src/main/java/gw/netty/http/HttpChannelInitializer.java)中配置:

```text
  +----------------------------------------+---------------+
  |                  ChannelPipeline       |               |
  |                                       \|/              |
  |  +----------------------+  +-----------+------------+  |
  |  | LoadBalancerHandler  |  |           -            |  |
  |  +----------+-----------+  +-----------+------------+  |
  |            /|\                         |               |
  |             |                         \|/              |
  |  +----------+-----------+  +-----------+------------+  |
  |  | HttpRouterHandler    |  |           -            |  |
  |  +----------+-----------+  +-----------+------------+  |
  |            /|\                         |               |
  |             |                         \|/              |
  |  +----------+-----------+  +-----------+------------+  |
  |  | PreInBoundHandler    |  |           -            |  |
  |  +----------+-----------+  +-----------+------------+  |
  |            /|\                         |               |
  |             |                         \|/              |
  |  +----------+-----------+  +-----------+------------+  |
  |  | ChunkedWriteHandler  |  |   PostOutboundHandler  |  |
  |  +----------+-----------+  +-----------+------------+  |
  |            /|\                         .               |
  |             .                          .               |
  |     [  channelRead() ]        [     write()      ]     |
  |     [ + INBOUND data ]        [ + OUTBOUND data  ]     |
  |             .                          .               |
  |             .                         \|/              |
  |  +----------+-----------+  +-----------+------------+  |
  |  | HttpObjectAggregator |  |   ChunkedWriteHandler  |  |
  |  +----------+-----------+  +-----------+------------+  |
  |            /|\                         |               |
  |             |                         \|/              |
  |  +----------+-----------+  +-----------+------------+  |
  |  |  HttpServerCodec     |  |     HttpServerCodec    |  |
  |  +----------+-----------+  +-----------+------------+  |
  |            /|\                         |               |
  +-------------+--------------------------+---------------+
                |                         \|/
  +-------------+--------------------------+---------------+
  |             |                          |               |
  |     [ Socket.read() ]          [ Socket.write() ]      |
  |                                                        |
  |  Netty Internal I/O Threads (Transport Implementation) |
  +--------------------------------------------------------+
```
