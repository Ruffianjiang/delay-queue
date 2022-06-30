package com.dawn.delayqueue.core;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 存放可以消费的job
 *
 * @author Yang WenJie
 * @date 2018/1/27 上午2:04
 */
public class ReadyQueue {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 添加jobId到准备队列
     *
     * @param topic
     * @param delayQueueJobId
     */
    public void pushToReadyQueue(String topic, long delayQueueJobId) {
        RBlockingQueue<Long> rBlockingQueue = redissonClient.getBlockingQueue(topic);
        rBlockingQueue.offer(delayQueueJobId);
    }

    /**
     * 从准备队列中获取jobId
     *
     * @param topic
     * @return
     */
    public Long pollFormReadyQueue(String topic) {
        RBlockingQueue<Long> rBlockingQueue = redissonClient.getBlockingQueue(topic);
        return rBlockingQueue.poll();
    }
}
