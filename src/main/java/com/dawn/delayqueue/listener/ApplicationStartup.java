package com.dawn.delayqueue.listener;

import com.dawn.delayqueue.core.DelayBucketHandler;
import com.dawn.delayqueue.core.DelayQueue;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ruffianjiang
 * @version V1.0
 * @Title: ApplicationStartup
 * @Package delay-queue
 * @date 2019/11/19 23:33
 */
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ExecutorService executorService = Executors.newFixedThreadPool((int)DelayQueue.DELAY_BUCKET_NUM);
        for (int i = 0; i < DelayQueue.DELAY_BUCKET_NUM; i++) {
            executorService.execute(new DelayBucketHandler(DelayQueue.DELAY_BUCKET_KEY_PREFIX+i));
        }
    }
}
