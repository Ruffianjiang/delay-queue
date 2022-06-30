package com.dawn.delayqueue.core;

import com.dawn.delayqueue.core.model.DelayQueueJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 扫描延迟任务桶中的任务，放到准备队列中
 *
 * @author Yang WenJie
 * @date 2018/1/27 下午8:03
 */
public class DelayQueueHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DelayQueueHandler.class);

    private DelayQueue delayQueue;

    private String topicName;


    private DelayQueueHandler() {
    }

    public DelayQueueHandler(DelayQueue delayQueue, String topicName) {
        this.delayQueue = delayQueue;
        this.topicName = topicName;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DelayQueueJob delayQueueJob = delayQueue.pop(topicName);

                //没有任务
                if (delayQueueJob == null) {
                    sleep();
                    continue;
                }
                delayQueue.finish(delayQueueJob.getId());

                logger.info("=================DelayQueueHandler[{}] 消费成功=======================", topicName);

            } catch (Exception e) {
                logger.error("消费出错：", e);
            }


        }
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            logger.error("消费间隔出错", e);
        }
    }
}
