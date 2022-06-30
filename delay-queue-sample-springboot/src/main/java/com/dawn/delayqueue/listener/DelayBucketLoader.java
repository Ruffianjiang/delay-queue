package com.dawn.delayqueue.listener;

import com.dawn.delayqueue.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 启动后台定时 (生产者)
 * @author Ruffianjiang
 * @version V1.0
 * @Title: DelayBucketLoader
 * @Package delay-queue
 * @date 2019/11/20 23:11
 */
@Component
public class DelayBucketLoader {

    private static final Logger logger = LoggerFactory.getLogger(DelayBucketLoader.class);
    @Autowired
    private DelayBucket delayBucket;

    @Autowired
    private DelayQueueJobPool delayQueueJobPool;

    @Autowired
    private ReadyQueue readyQueue;


    @PostConstruct
    public void init() {
        ExecutorService executorService = Executors.newFixedThreadPool((int) DelayQueueConstant.DELAY_BUCKET_NUM);
        for (int i = 0; i < DelayQueueConstant.DELAY_BUCKET_NUM; i++) {
            executorService.execute(new DelayBucketHandler(DelayQueueConstant.DELAY_BUCKET_KEY_PREFIX + i,
                    delayBucket, delayQueueJobPool, readyQueue));
        }
        logger.info("=================DelayBucketLoader 初始化成功=======================");
    }


}
