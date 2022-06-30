package com.dawn.delayqueue.core;

import com.dawn.delayqueue.core.model.DelayQueueJob;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 延迟任务池
 *
 * @author Yang WenJie
 * @date 2018/1/27 上午1:35
 */
public class DelayQueueJobPool {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 查询 DelayQueueJob
     *
     * @param delayQueueJobId
     * @return
     */
    public DelayQueueJob getDelayQueueJob(long delayQueueJobId) {
        RMap<Long, DelayQueueJob> rMap = redissonClient.getMap(DelayQueueConstant.DELAY_QUEUE_JOB_POOL);
        return rMap.get(delayQueueJobId);
    }

    /**
     * 添加 DelayQueueJob
     *
     * @param delayQueueJob
     */
    public void addDelayQueueJob(DelayQueueJob delayQueueJob) {
        RMap<Long, DelayQueueJob> rMap = redissonClient.getMap(DelayQueueConstant.DELAY_QUEUE_JOB_POOL);
        rMap.put(delayQueueJob.getId(), delayQueueJob);
    }

    /**
     * 删除 DelayQueueJob
     *
     * @param delayQueueJobId
     */
    public void deleteDelayQueueJob(long delayQueueJobId) {
        RMap<Long, DelayQueueJob> rMap = redissonClient.getMap(DelayQueueConstant.DELAY_QUEUE_JOB_POOL);
        rMap.remove(delayQueueJobId);
    }
}
