package com.dawn.delayqueue.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 扫描延迟任务桶中的任务，放到准备队列中
 * @author Yang WenJie
 * @date 2018/1/27 下午8:03
 */
public class DelayBucketHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DelayBucketHandler.class);

    private String delayBucketKey;

    public DelayBucketHandler(String delayBucketKey) {
        this.delayBucketKey = delayBucketKey;
    }

    public String getDelayBucketKey() {
        return delayBucketKey;
    }

    public void setDelayBucketKey(String delayBucketKey) {
        this.delayBucketKey = delayBucketKey;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ScoredSortedItem item = DelayBucket.getFromBucket(this.delayBucketKey);
                //没有任务
                if (item == null) {
                    sleep();
                    continue;
                }
                //延迟时间没到
                if (item.getDelayTime() > System.currentTimeMillis()) {
                    sleep();
                    continue;
                }

                DelayQueueJob delayQueueJob = DelayQueueJobPool.getDelayQueueJob(item.getDelayQueueJobId());
                //延迟任务元数据不存在
                if (delayQueueJob == null) {
                    DelayBucket.deleteFormBucket(this.delayBucketKey,item);
                    continue;
                }

                //再次确认延时时间是否到了
                if (delayQueueJob.getDelayTime() > System.currentTimeMillis()) {
                    //删除旧的
                    DelayBucket.deleteFormBucket(this.delayBucketKey,item);
                    //重新计算延迟时间
                    DelayBucket.addToBucket(this.delayBucketKey,new ScoredSortedItem(delayQueueJob.getId(),delayQueueJob.getDelayTime()));
                } else {
                    ReadyQueue.pushToReadyQueue(delayQueueJob.getTopic(),delayQueueJob.getId());
                    DelayBucket.deleteFormBucket(this.delayBucketKey,item);
                }

            }catch (Exception e) {
                logger.error("扫描delaybucket出错：",e);
            }


        }
    }

    private void sleep(){
        try {
            TimeUnit.SECONDS.sleep(1L);
        }catch (InterruptedException e){
            logger.error("",e);
        }
    }
}
