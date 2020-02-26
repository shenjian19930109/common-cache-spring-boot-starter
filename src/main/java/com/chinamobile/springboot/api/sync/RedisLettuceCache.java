package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.config.CacheConfig;
import com.chinamobile.springboot.exception.CacheConfigException;
import com.chinamobile.springboot.lettuce.LettuceConnectionManager;
import com.chinamobile.springboot.lettuce.RedisLettuceCacheConfig;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.async.RedisKeyAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import io.lettuce.core.api.sync.RedisKeyCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 22:39
 */
@Slf4j
public abstract class RedisLettuceCache extends AbstractExternalCache {

    private RedisLettuceCacheConfig config;

    protected Function<Object, byte[]> valueEncoder;
    protected Function<byte[], Object> valueDecoder;
    private AbstractRedisClient redisClient;
//    private StatefulConnection connection;

    private LettuceConnectionManager connectionManager;

    protected RedisStringCommands<byte[], byte[]> commands;
    protected RedisKeyCommands<byte[], byte[]> keyCommands;
    protected RedisStringAsyncCommands<byte[], byte[]> asyncCommands;
    protected RedisKeyAsyncCommands<byte[], byte[]> keyAsyncCommands;

    public RedisLettuceCache(RedisLettuceCacheConfig config) {
        super(config);
        this.config = config;
        if (config.getRedisClient() == null) {
            throw new CacheConfigException("RedisClient is required");
        }
        valueEncoder = config.getValueEncoder();
        valueDecoder = config.getValueDecoder();
        redisClient = config.getRedisClient();
//        connection = config.getConnection();===

        connectionManager = LettuceConnectionManager.defaultManager();
        connectionManager.init(redisClient, config.getConnection());

        commands = (RedisStringCommands<byte[], byte[]>) connectionManager.commands(redisClient);
        keyCommands = (RedisKeyCommands<byte[], byte[]>) this.commands;
        asyncCommands = (RedisStringAsyncCommands<byte[], byte[]>) connectionManager.asyncCommands(redisClient);
        keyAsyncCommands = (RedisKeyAsyncCommands<byte[], byte[]>) this.asyncCommands;

    }

    @Override
    public CacheConfig config() {
        return config;
    }
}
