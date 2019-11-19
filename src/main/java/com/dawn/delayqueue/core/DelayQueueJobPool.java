package com.dawn.delayqueue.core;

import com.dawn.delayqueue.util.RedissonUtils;
import org.redisson.api.RMap;

/**
 * 延迟任务池
 * @author Yang WenJie
 * @date 2018/1/27 上午1:35
 */
public class DelayQueueJobPool {

    private static final String DELAY_QUEUE_JOB_POOL = "delayQueueJobPool";

    /**
     * 查询 DelayQueueJob
     * @param delayQueueJobId
     * @return
     */
    public static DelayQueueJob getDelayQueueJob(long delayQueueJobId) {
        RMap<Long, DelayQueueJob> rMap = RedissonUtils.getMap(DELAY_QUEUE_JOB_POOL);
        return rMap.get(delayQueueJobId);
    }

    /**
     * 添加 DelayQueueJob
     * @param delayQueueJob
     */
    public static void addDelayQueueJob(DelayQueueJob delayQueueJob) {
        RMap<Long, DelayQueueJob> rMap = RedissonUtils.getMap(DELAY_QUEUE_JOB_POOL);
        rMap.put(delayQueueJob.getId(), delayQueueJob);
    }

    /**
     * 删除 DelayQueueJob
     * @param delayQueueJobId
     */
    public static void deleteDelayQueueJob(long delayQueueJobId) {
        RMap<Long, DelayQueueJob> rMap = RedissonUtils.getMap(DELAY_QUEUE_JOB_POOL);
        rMap.remove(delayQueueJobId);
    }
}
