package com.dawn.delayqueue.redisson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;


@SpringBootApplication(exclude = {RedisAutoConfiguration.class,})
public class DelayQueueRedissonApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelayQueueRedissonApplication.class, args);
    }

}
