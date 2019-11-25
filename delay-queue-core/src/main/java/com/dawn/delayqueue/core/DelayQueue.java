package com.dawn.delayqueue.core;

import com.dawn.delayqueue.core.model.DelayQueueJob;
import com.dawn.delayqueue.core.model.ScoredSortedItem;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 延迟消息队列
 * @author Yang WenJie
 * @date 2018/1/27 上午11:28
 */
public class DelayQueue {

    private static final Logger logger = LoggerFactory.getLogger(DelayQueue.class);


    private DelayBucket delayBucket;

    private DelayQueueJobPool delayQueueJobPool;

    private ReadyQueue readyQueue;

    /**
     * 获取delayBucket key 分开多个，有利于提高效率
     * @param delayQueueJobId
     * @return
     */
    private String getDelayBucketKey(long delayQueueJobId) {
        return DelayQueueConstant.DELAY_BUCKET_KEY_PREFIX + Math.floorMod(delayQueueJobId, DelayQueueConstant.DELAY_BUCKET_NUM);
    }

    /**
     * 添加延迟任务到延迟队列
     * @param delayQueueJob
     */
    public void push(DelayQueueJob delayQueueJob) {
        delayQueueJobPool.addDelayQueueJob(delayQueueJob);
        ScoredSortedItem item = new ScoredSortedItem(delayQueueJob.getId(), delayQueueJob.getDelayTime());
        delayBucket.addToBucket(getDelayBucketKey(delayQueueJob.getId()),item);
    }

    /**
     * 获取准备好的延迟任务
     * @param topic
     * @return
     */
    public DelayQueueJob pop(String topic) {
        Long delayQueueJobId = readyQueue.pollFormReadyQueue(topic);
        if (delayQueueJobId == null) {
            return null;
        } else {
            DelayQueueJob delayQueueJob = delayQueueJobPool.getDelayQueueJob(delayQueueJobId);
            if (delayQueueJob == null) {
                return null;
            } else {
                long delayTime = delayQueueJob.getDelayTime();
                //获取消费超时时间，重新放到延迟任务桶中
                long reDelayTime = System.currentTimeMillis()+delayQueueJob.getTtrTime()*1000L;
                delayQueueJob.setDelayTime(reDelayTime);
                delayQueueJobPool.addDelayQueueJob(delayQueueJob);
                ScoredSortedItem item = new ScoredSortedItem(delayQueueJob.getId(), reDelayTime);
                delayBucket.addToBucket(getDelayBucketKey(delayQueueJob.getId()),item);
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
    public void delete(long delayQueueJobId) {
        delayQueueJobPool.deleteDelayQueueJob(delayQueueJobId);
    }

    /**
     *
     * @param delayQueueJobId
     */
    public void finish(long delayQueueJobId) {
        DelayQueueJob delayQueueJob = delayQueueJobPool.getDelayQueueJob(delayQueueJobId);
        if (delayQueueJob == null) {
            return;
        }
        delayQueueJobPool.deleteDelayQueueJob(delayQueueJobId);
        ScoredSortedItem item = new ScoredSortedItem(delayQueueJob.getId(), delayQueueJob.getDelayTime());
        delayBucket.deleteFormBucket(getDelayBucketKey(delayQueueJob.getId()),item);
    }

    /**
     * 查询delay job
     * @param delayQueueJobId
     * @return
     */
    public DelayQueueJob get(long delayQueueJobId) {
        return delayQueueJobPool.getDelayQueueJob(delayQueueJobId);
    }
}
