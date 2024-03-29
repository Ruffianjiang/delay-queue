# delay-queue

**推荐使用MQ的延迟队列功能，相对稳定，实现简单**


redis实现延迟消息队列 Spring boot

1. [delay-queue-core](delay-queue-core)
2. [delay-queue-sample-springboot](delay-queue-sample-springboot)

1和2为 java 实现，客户端定时扫面

3. [delay-queue-redisson](delay-queue-redisson)  

3为 redisson 的 RBlockingQueue 实现，阻塞获取




## 修改说明
基于 spring boot 的重制版，对生产者、消费者进行了修改，实现定时的推送功能  
**base on [delay-queue](https://github.com/yangwenjie88/delay-queue)**


#### 1、swagger地址：

[http://localhost:18080/swagger-ui.html#/delay-queue-controller](http://localhost:18080/swagger-ui.html#/delay-queue-controller)

#### 2、启用注解
@EnableDelayQueue  

>~~TODO~~ 可实现 starter


#### 3、实现redissonClient的注入
示例：`RedissonConfig #RedissonClient`

#### 4、项目启动加入生产者和消费者
DelayBucketLoader  
DelayCustomerLoader  

#### 5、其他说明  
1、仅供学习参考  
2、小项目轻量级可以进行过渡，需加入DB记录 ~~TODO~~  
3、生产建议使用MQ的延迟队列，或者使用分布式job平台进行调度  

=======================以下为原项目引用=======================

## todo or defect
1、分布式消费（未验证）  
2、任务去重机制  
3、多线程消费  
4、相同时间点分组推送  
5、修改线程为阻塞模式  
6、消费推送异步  
7、延时队列流程监控  


## 需求背景
&nbsp;&nbsp;&nbsp;&nbsp;最近在做一个排队取号的系统
* 在用户预约时间到达前XX分钟发短信通知
* 在用户预约时间结束时要判断用户是否去取号了，不然就记录为爽约
* 在用户取号后开始，等待XX分钟后要发短信提醒是否需要使用其他渠道办理

类似的场景太多，最简单的解决办法就是定时任务去扫表。这样每个业务都要维护自己的扫表逻辑，
而且数据量越来越来越多的，有的数据可能会延迟比较大

经过一番搜索，网上说rabbitmq可以满足延迟执行需求，但是目前系统用了其他消息中间件，所以不打算用。

基于Redis实现的延迟消息队列java版：[delay-queue](https://github.com/yangwenjie88/delay-queue)

## 整体结构
整个延迟队列由4个部分组成：

1. JobPool用来存放所有Job的元信息。
2. DelayBucket是一组以时间为维度的有序队列，用来存放所有需要延迟的Job（这里只存放Job Id）。
3. Timer负责实时扫描各个Bucket，并将delay时间大于等于当前时间的Job放入到对应的Ready Queue。
4. ReadyQueue存放处于Ready状态的Job（这里只存放JobId），以供消费程序消费。

![结构图](https://tech.youzan.com/content/images/2016/03/all-1.png)

## 消息结构
每个Job必须包含一下几个属性：

1. topic：Job类型。可以理解成具体的业务名称。
2. id：Job的唯一标识。用来检索和删除指定的Job信息。
3. delayTime：jod延迟执行的时间，13位时间戳
4. ttr（time-to-run)：Job执行超时时间。单位：秒。主要是为了消息可靠性
5. message：Job的内容，供消费者做具体的业务处理，以json格式存储。

## 举例说明一个Job的生命周期
1. 用户预约后，同时往JobPool里put一个job。job结构为：{‘topic':'book’, ‘id':'123456’, ‘delayTime’:1517069375398 ,’ttrTime':60 , ‘message':’XXXXXXX’}
  同时以jobId作为value，delayTime作为score 存到bucket 中，用jodId取模，放到10个bucket中，以提高效率
  
2. timer每时每刻都在轮询各个bucket，按照score排序去最小的一个，当delayTime < 当前时间后，，取得job id从job pool中获取元信息。
如果这时该job处于deleted状态，则pass，继续做轮询；如果job处于非deleted状态，首先再次确认元信息中delay是否大于等于当前时间，
如果满足则根据topic将jobId放入对应的ready queue，然后从bucket中移除,并且；如果不满足则重新计算delay时间，再次放入bucket，并将之前的job id从bucket中移除。

3. 消费端轮询对应的topic的ready queue，获取job后做自己的业务逻辑。与此同时，服务端将已经被消费端获取的job按照其设定的TTR，重新计算执行时间，并将其放入bucket。
消费端处理完业务后向服务端响应finish，服务端根据job id删除对应的元信息。如果消费端在ttr时间内没有响应，则ttr时间后会再收到该消息

## 后续扩展
1. 加上超时重发次数

实现思路
任务job内容包含Array{0,0,2m,10m,10m,1h,2h,6h,15h}和通知到第几次N(这里N=1, 即第1次).
消费者从队列中取出任务, 根据N取得对应的时间间隔为0, 立即发送通知.

第1次通知失败, N += 1 => 2
从Array中取得间隔时间为2m, 添加一个延迟时间为2m的任务到延迟队列, 任务内容仍包含Array和N

第2次通知失败, N += 1 => 3, 取出对应的间隔时间10m, 添加一个任务到延迟队列, 同上
......
第7次通知失败, N += 1 => 8, 取出对应的间隔时间15h, 添加一个任务到延迟队列, 同上
第8次通知失败, N += 1 => 9, 取不到间隔时间, 结束通知

## 引用说明
参考[有赞延迟队列](https://tech.youzan.com/queuing_delay/)思路设计实现