package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.config.RedisLettuceCacheConfig;
import com.chinamobile.springboot.exception.CacheConfigException;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 22:39
 */
public class RedisLettuceCache extends AbstractExternalCache {

    private RedisLettuceCacheConfig config;

    private Function<Object, byte[]> valueEncoder;
    private Function<byte[], Object> valueDecoder;
    private AbstractRedisClient redisClient;
    private StatefulRedisConnection connection;

    public RedisLettuceCache(RedisLettuceCacheConfig config) {
        this.config = config;
        if (config.getRedisClient() == null) {
            throw new CacheConfigException("RedisClient is required");
        }
        redisClient = config.getRedisClient();
        connection = config.getConnection();



    }
}
