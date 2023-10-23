package com.dawn.delayqueue.redisson.listener;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;

@Slf4j
@Component
public class DelayQueueExec {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RBlockingQueue<String> queue;

    @PostConstruct
    public void init() {
        new Thread(() -> {

            while (!redissonClient.isShutdown() && !redissonClient.isShuttingDown()) {
                String message = null;
                try {
                    // 停止时，报错Redisson is shutdown，原因take还在阻塞获取，Redisson链接断开
                    message = queue.take();
                    log.info("now:{}, message: {}", DateFormat.getDateTimeInstance().format(new Date()), message);
                } catch (InterruptedException e) {
                    log.error("error", e);
                }
            }
        }).start();
    }

}
