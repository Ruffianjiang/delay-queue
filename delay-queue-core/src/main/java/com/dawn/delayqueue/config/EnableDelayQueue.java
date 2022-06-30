package com.dawn.delayqueue.config;

import com.dawn.delayqueue.core.DelayBucket;
import com.dawn.delayqueue.core.DelayQueue;
import com.dawn.delayqueue.core.DelayQueueJobPool;
import com.dawn.delayqueue.core.ReadyQueue;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author jiangyj
 * @version 1.0
 * @create 2021-09-29 18:26
 **/
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Import({DelayBucket.class, DelayQueue.class, DelayQueueJobPool.class, ReadyQueue.class})
@Documented
public @interface EnableDelayQueue {
}
