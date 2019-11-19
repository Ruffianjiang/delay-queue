package com.dawn.delayqueue.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 延迟消息队列
 * @author Yang WenJie
 * @date 2018/1/27 上午11:28
 */
public class DelayQueue {

    private static final Logger logger = LoggerFactory.getLogger(DelayQueue.class);
    public static final String DELAY_BUCKET_KEY_PREFIX = "delayBucket";
    public static final long  DELAY_BUCKET_NUM = 10L;

    /**
     * 获取delayBucket key 分开多个，有利于提高效率
     * @param delayQueueJobId
     * @return
     */
    private static String getDelayBucketKey(long delayQueueJobId) {
        return DELAY_BUCKET_KEY_PREFIX+ Math.floorMod(delayQueueJobId,DELAY_BUCKET_NUM);
    }

    /**
     * 添加延迟任务到延迟队列
     * @param delayQueueJob
     */
    public static void push(DelayQueueJob delayQueueJob) {
        DelayQueueJobPool.addDelayQueueJob(delayQueueJob);
        ScoredSortedItem item = new ScoredSortedItem(delayQueueJob.getId(), delayQueueJob.getDelayTime());
        DelayBucket.addToBucket(getDelayBucketKey(delayQueueJob.getId()),item);
    }

    /**
     * 获取准备好的延迟任务
     * @param topic
     * @return
     */
    public static DelayQueueJob pop(String topic) {
        Long delayQueueJobId = ReadyQueue.pollFormReadyQueue(topic);
        if (delayQueueJobId == null) {
            return null;
        } else {
            DelayQueueJob delayQueueJob = DelayQueueJobPool.getDelayQueueJob(delayQueueJobId);
            if (delayQueueJob == null) {
                return null;
            } else {
                long delayTime = delayQueueJob.getDelayTime();
                //获取消费超时时间，重新放到延迟任务桶中
                long reDelayTime = System.currentTimeMillis()+delayQueueJob.getTtrTime()*1000L;
                delayQueueJob.setDelayTime(reDelayTime);
                DelayQueueJobPool.addDelayQueueJob(delayQueueJob);
                ScoredSortedItem item = new ScoredSortedItem(delayQueueJob.getId(), reDelayTime);
                DelayBucket.addToBucket(getDelayBucketKey(delayQueueJob.getId()),item);
                //返回的时候设置回
                delayQueueJob.setDelayTime(delayTime);
                return delayQueueJob;
            }
        }
    }

    /**
     * 删除延迟队列任务
     * @param delayQueueJobId
     */
    public static void delete(long delayQueueJobId) {
        DelayQueueJobPool.deleteDelayQueueJob(delayQueueJobId);
    }

    /**
     *
     * @param delayQueueJobId
     */
    public static void finish(long delayQueueJobId) {
        DelayQueueJob delayQueueJob = DelayQueueJobPool.getDelayQueueJob(delayQueueJobId);
        if (delayQueueJob == null) {
            return;
        }
        DelayQueueJobPool.deleteDelayQueueJob(delayQueueJobId);
        ScoredSortedItem item = new ScoredSortedItem(delayQueueJob.getId(), delayQueueJob.getDelayTime());
        DelayBucket.deleteFormBucket(getDelayBucketKey(delayQueueJob.getId()),item);
    }

    /**
     * 查询delay job
     * @param delayQueueJobId
     * @return
     */
    public static DelayQueueJob get(long delayQueueJobId) {
        return DelayQueueJobPool.getDelayQueueJob(delayQueueJobId);
    }
}
