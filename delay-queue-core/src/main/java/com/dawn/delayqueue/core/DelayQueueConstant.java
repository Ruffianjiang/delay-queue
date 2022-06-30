package com.dawn.delayqueue.core;

/**
 * @author Ruffianjiang
 * @version V1.0
 * @Title: DelayQueueConstant
 * @Package delay-queue
 * @date 2019/11/20 22:57
 */
public class DelayQueueConstant {

    // 延时桶
    public static final String DELAY_BUCKET_KEY_PREFIX = "delayQueue:delayBucket";

    // 延时桶的数量
    public static final long DELAY_BUCKET_NUM = 10L;

    // 延时任务池
    public static final String DELAY_QUEUE_JOB_POOL = "delayQueue:delayQueueJobPool";


}
