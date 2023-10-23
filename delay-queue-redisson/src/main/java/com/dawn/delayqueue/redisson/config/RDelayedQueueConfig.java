package com.dawn.delayqueue.redisson.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RDelayedQueueConfig {

    /**
     * 创建基本队列
     * @param redissonClient
     * @return
     */
    @Bean
    public RBlockingQueue<String> queue(RedissonClient redissonClient) {
        log.info("queue注入成功！！");
        return redissonClient.getBlockingQueue("redisson_delay_Queue");
    }

    /**
     * 延迟队列
     * @param redissonClient
     * @param queue
     * @return
     */
    @Bean
    public RDelayedQueue<String> delayedQueue(RedissonClient redissonClient, RBlockingQueue<String> queue ) {
        log.info("delayedQueue注入成功！！");
        // 创建延迟队列并关联到基本队列
        return redissonClient.getDelayedQueue(queue);
    }


}
