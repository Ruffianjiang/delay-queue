package com.dawn.delayqueue.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ruffianjiang
 * @version V1.0
 * @Title: RedissonConfig
 * @Package delay-queue
 * @date 2019/11/20 0:27
 */
@Configuration
@ConditionalOnProperty("${spring.redis}")
public class RedissonConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedissonConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database:0}")
    private int database;

    private int connectTimeout = 10000;

    @Value("${spring.redis.timeout:3000}")
    private int timeout;


    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        logger.info("RedissonClient注入成功！！");
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + this.host + ":" + this.port)
                .setDatabase(this.database)
                .setConnectTimeout(this.connectTimeout)
                .setPassword(this.password)
                .setTimeout(this.timeout);
        RedissonClient redissonClient = Redisson.create(config);
        logger.info("redis地址：{}:{}", this.host, this.port);
        return redissonClient;
    }

}
