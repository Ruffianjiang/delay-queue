package com.dawn.delayqueue;

import com.dawn.delayqueue.config.EnableDelayQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableDelayQueue
@SpringBootApplication
public class DelayQueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelayQueueApplication.class, args);
    }

}
