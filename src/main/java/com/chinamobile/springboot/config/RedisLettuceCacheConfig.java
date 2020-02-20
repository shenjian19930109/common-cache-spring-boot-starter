package com.chinamobile.springboot.config;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 22:30
 */
@Getter
@Setter
public class RedisLettuceCacheConfig extends CacheConfig {

    private AbstractRedisClient redisClient;

    private StatefulRedisConnection connection;

}
