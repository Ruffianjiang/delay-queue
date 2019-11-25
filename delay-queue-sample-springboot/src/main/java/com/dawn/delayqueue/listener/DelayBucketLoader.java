package com.dawn.delayqueue.listener;

import com.dawn.delayqueue.core.DelayBucket;
import com.dawn.delayqueue.core.DelayBucketHandler;
import com.dawn.delayqueue.core.DelayQueueConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 启动后台定时
 * @author Ruffianjiang
 * @version V1.0
 * @Title: DelayBucketLoader
 * @Package delay-queue
 * @date 2019/11/20 23:11
 */
@Component
public class DelayBucketLoader {

    @Autowired
    private DelayBucket delayBucket;


    @PostConstruct
    public void init() {
        ExecutorService executorService = Executors.newFixedThreadPool((int) DelayQueueConstant.DELAY_BUCKET_NUM);
        for (int i = 0; i < DelayQueueConstant.DELAY_BUCKET_NUM; i++) {
            executorService.execute(new DelayBucketHandler(DelayQueueConstant.DELAY_BUCKET_KEY_PREFIX + i, delayBucket));
        }
    }


}
