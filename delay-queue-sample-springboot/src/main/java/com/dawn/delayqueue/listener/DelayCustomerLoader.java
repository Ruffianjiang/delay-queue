package com.dawn.delayqueue.listener;

import com.dawn.delayqueue.core.DelayQueue;
import com.dawn.delayqueue.core.DelayQueueConstant;
import com.dawn.delayqueue.core.DelayQueueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 根据topic消费(消费者)
 * @author jiangyj
 * @version 1.0
 * @create 2021-09-29 19:01
 **/
@Component
public class DelayCustomerLoader {

    private static final Logger logger = LoggerFactory.getLogger(DelayCustomerLoader.class);

    @Autowired
    private DelayQueue delayQueue;

    private String topicName = "ql";


    @PostConstruct
    public void init() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 0; i < DelayQueueConstant.DELAY_BUCKET_NUM; i++) {
            executorService.execute(new DelayQueueHandler(delayQueue, topicName));
        }
        logger.info("=================DelayCustomerLoader 初始化成功=======================");
    }


}
