package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.common.enums.CacheResultCode;
import com.chinamobile.springboot.config.CacheConfig;
import com.chinamobile.springboot.exception.CacheConfigException;
import com.chinamobile.springboot.lettuce.LettuceConnectionManager;
import com.chinamobile.springboot.lettuce.RedisLettuceCacheConfig;
import com.chinamobile.springboot.result.CacheGetResult;
import com.chinamobile.springboot.result.CacheResult;
import com.chinamobile.springboot.result.ResultData;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.async.RedisKeyAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import io.lettuce.core.api.sync.RedisKeyCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
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

    @Override
    protected CacheGetResult<String> do_GET(String key) {

        try {
            byte[] newKey = buildKey(key);
            RedisFuture<byte[]> future = asyncCommands.get(newKey);
            CacheGetResult result = new CacheGetResult(future.handle((valueBytes, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                } else {
                    if (valueBytes != null) {
                        return new ResultData(CacheResultCode.SUCCESS, null, valueDecoder.apply(valueBytes));
                    }else {
                        return new ResultData(CacheResultCode.NOT_EXISTS, null, null);
                    }
                }
            }));
            return result;
        }catch (Exception e) {
            log.error("do_GET : (" + key + "), exception : ", e);
            return new CacheGetResult<>(e);
        }
    }

    @Override
    protected CacheResult do_SET(String key, String value, long expire, TimeUnit timeUnit) {

        try {
            byte[] newKey = buildKey(key);
            RedisFuture<String> future = asyncCommands.psetex(newKey, timeUnit.toMillis(expire), valueEncoder.apply(value));
            CacheResult cacheResult = new CacheResult(future.handle((result, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                }else {
                    if (result.equalsIgnoreCase("OK")) {
                        return new ResultData(CacheResultCode.SUCCESS, null, null);
                    }else {
                        return new ResultData(CacheResultCode.FAIL, result, null);
                    }
                }
            }));
            return cacheResult;
        }catch (Exception e) {
            log.error("do_SET : (" + key + ", " + value + "), exception : ", e);
            return new CacheResult(e);
        }
    }

    @Override
    protected CacheResult do_SET_IF_NOT_EXIST(String key, String value, long expire, TimeUnit timeUnit) {

        try {
            byte[] newKey = buildKey(key);
            RedisFuture<String> future = asyncCommands.set(newKey, valueEncoder.apply(value), SetArgs.Builder.nx().px(timeUnit.toMillis(expire)));
            CacheResult cacheResult = new CacheResult(future.handle((result, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                }else {
                    if (result == null) {
                        return new ResultData(CacheResultCode.ALREADY_EXISTS, null, null);
                    }else if (result.equalsIgnoreCase("OK")) {
                        return new ResultData(CacheResultCode.SUCCESS, null, null);
                    }else {
                        return new ResultData(CacheResultCode.FAIL, null, null);
                    }
                }
            }));
            return cacheResult;
        }catch (Exception e) {
            log.error("do_SET_IF_NOT_EXIST : (" + key + ", " + value + "), exception : ", e);
            return new CacheResult(e);
        }
    }

    @Override
    protected CacheResult do_SET_IF_EXIST(String key, String value, long expire, TimeUnit timeUnit) {

        try {
            byte[] newKey = buildKey(key);
            RedisFuture<String> future = asyncCommands.set(newKey, valueEncoder.apply(value), SetArgs.Builder.xx().px(timeUnit.toMillis(expire)));
            CacheResult cacheResult = new CacheResult(future.handle((result, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                }else {
                    if (result == null) {
                        return new ResultData(CacheResultCode.NOT_EXISTS, null, null);
                    }else if (result.equalsIgnoreCase("OK")) {
                        return new ResultData(CacheResultCode.SUCCESS, null, null);
                    }else {
                        return new ResultData(CacheResultCode.FAIL, null, null);
                    }
                }
            }));
            return cacheResult;
        }catch (Exception e) {
            log.error("do_SET_IF_EXIST : (" + key + ", " + value + "), exception : ", e);
            return new CacheResult(e);
        }
    }

    @Override
    protected CacheResult do_DELETE(String key) {

        try {
            byte[] newKey = buildKey(key);
            RedisFuture<Long> future = keyAsyncCommands.del(newKey);
            CacheResult cacheResult = new CacheResult(future.handle((result, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                }else {
                    if (result == 1) {
                        return new ResultData(CacheResultCode.SUCCESS, null, null);
                    }else if (result == 0) {
                        return new ResultData(CacheResultCode.NOT_EXISTS, null, null);
                    }else {
                        return new ResultData(CacheResultCode.FAIL, null, null);
                    }
                }
            }));
            return cacheResult;
        }catch (Exception e) {
            log.error("do_DELETE : (" + key + "), exception : ", e);
            return new CacheResult(e);
        }
    }
}
