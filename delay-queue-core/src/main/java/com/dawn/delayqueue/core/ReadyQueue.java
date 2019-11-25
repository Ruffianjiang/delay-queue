package com.dawn.delayqueue.core;

import com.dawn.delayqueue.util.RedissonUtils;
import org.redisson.api.RBlockingQueue;

/**
 * 存放可以消费的job
 * @author Yang WenJie
 * @date 2018/1/27 上午2:04
 */
public class ReadyQueue {

    /**
     * 添加jobId到准备队列
     * @param topic
     * @param delayQueueJobId
     */
    public void pushToReadyQueue(String topic, long delayQueueJobId) {
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(topic);
        rBlockingQueue.offer(delayQueueJobId);
    }

    /**
     * 从准备队列中获取jobId
     * @param topic
     * @return
     */
    public Long pollFormReadyQueue(String topic) {
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(topic);
        return rBlockingQueue.poll();
    }
}
