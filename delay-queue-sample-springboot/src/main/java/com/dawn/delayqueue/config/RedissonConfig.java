package com.dawn.delayqueue.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Ruffianjiang
 * @version V1.0
 * @Title: RedissonConfig
 * @Package delay-queue
 * @date 2019/11/20 0:27
 */
@Configuration
public class RedissonConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedissonConfig.class);

    @Bean
    public RedissonClient redisson() {
        // 本例子使用的是yaml格式的配置文件，读取使用Config.fromYAML，如果是Json文件，则使用Config.fromJSON
        Config config = null;
        try {
            config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-config.yml"));
        } catch (IOException e) {
            logger.error("error", e);
            System.exit(0);
        }
        return Redisson.create(config);
    }

}
