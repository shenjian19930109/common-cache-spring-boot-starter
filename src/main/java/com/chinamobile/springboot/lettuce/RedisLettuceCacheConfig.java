package com.chinamobile.springboot.lettuce;

import com.chinamobile.springboot.config.ExternalCacheConfig;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.StatefulConnection;
import lombok.Data;

import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 22:30
 */
@Data
public class RedisLettuceCacheConfig extends ExternalCacheConfig {

    private AbstractRedisClient redisClient;

    private StatefulConnection connection;

    public RedisLettuceCacheConfig(Function<Object, Object> keyConvertor, Function<Object, byte[]> valueEncoder,
                                   Function<byte[], Object> valueDecoder, AbstractRedisClient redisClient, StatefulConnection connection) {
        super(keyConvertor, valueEncoder, valueDecoder);
        this.redisClient = redisClient;
        this.connection = connection;
    }
}
