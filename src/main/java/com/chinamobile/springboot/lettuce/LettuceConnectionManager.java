package com.chinamobile.springboot.lettuce;

import com.chinamobile.springboot.exception.CacheConfigException;
import com.chinamobile.springboot.exception.CacheException;
import com.chinamobile.springboot.support.DefaultCacheCodec;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/21 11:15
 */
public class LettuceConnectionManager {

    private static class LettuceObjects {
        private StatefulConnection connection;
        private Object commands;
        private Object asyncCommands;
        private Object reactiveCommands;
    }

    private LettuceConnectionManager() {
    }

    private static final LettuceConnectionManager defaultManager = new LettuceConnectionManager();

    public static LettuceConnectionManager defaultManager() {
        return defaultManager;
    }

    private Map<AbstractRedisClient, LettuceObjects> map = Collections.synchronizedMap(new WeakHashMap());

    public void init(AbstractRedisClient client, StatefulConnection connection) {
        map.computeIfAbsent(client, key -> {
           LettuceObjects lo = new LettuceObjects();
           lo.connection = connection;
           return lo;
        });
    }

    private LettuceObjects getLettuceObjectsFromMap(AbstractRedisClient redisClient) {
        LettuceObjects lo = map.get(redisClient);
        if (lo == null) {
            throw new CacheException("LettuceObjects is not initialized");
        }
        return lo;
    }

    public StatefulConnection connection(AbstractRedisClient redisClient) {
        LettuceObjects lo = getLettuceObjectsFromMap(redisClient);
        if (lo.connection == null) {
            if (redisClient instanceof RedisClient) {
                lo.connection = ((RedisClient) redisClient).connect(new DefaultCacheCodec());
            } else if (redisClient instanceof RedisClusterClient) {
                lo.connection = ((RedisClusterClient) redisClient).connect(new DefaultCacheCodec());
            } else {
                throw new CacheConfigException("type " + redisClient.getClass() + " is not supported");
            }
        }
        return lo.connection;
    }

    public Object commands(AbstractRedisClient redisClient) {
        connection(redisClient);
        LettuceObjects lo = getLettuceObjectsFromMap(redisClient);
        if (lo.commands == null) {
            if (lo.connection instanceof StatefulRedisConnection) {
                lo.commands = ((StatefulRedisConnection) lo.connection).sync();
            } else if (lo.connection instanceof StatefulRedisClusterConnection) {
                lo.commands = ((StatefulRedisClusterConnection) lo.connection).sync();
            } else if (lo.connection instanceof StatefulRedisSentinelConnection) {
                lo.commands = ((StatefulRedisSentinelConnection) lo.connection).sync();
            }else {
                throw new CacheConfigException("type " + lo.connection.getClass() + " is not supported");
            }
        }
        return lo.commands;
    }


    public Object asyncCommands(AbstractRedisClient redisClient) {
        connection(redisClient);
        LettuceObjects lo = getLettuceObjectsFromMap(redisClient);
        if (lo.asyncCommands == null) {
            if (lo.connection instanceof StatefulRedisConnection) {
                lo.asyncCommands = ((StatefulRedisConnection) lo.connection).async();
            } else if (lo.connection instanceof StatefulRedisClusterConnection) {
                lo.asyncCommands = ((StatefulRedisClusterConnection) lo.connection).async();
            } else if (lo.connection instanceof StatefulRedisSentinelConnection) {
                lo.asyncCommands = ((StatefulRedisSentinelConnection) lo.connection).async();
            } else {
                throw new CacheConfigException("type " + lo.connection.getClass() + " is not supported");
            }
        }
        return lo.asyncCommands;
    }

    public Object reactiveCommands(AbstractRedisClient redisClient) {
        connection(redisClient);
        LettuceObjects lo = getLettuceObjectsFromMap(redisClient);
        if (lo.reactiveCommands == null) {
            if (lo.connection instanceof StatefulRedisConnection) {
                lo.reactiveCommands = ((StatefulRedisConnection) lo.connection).reactive();
            } else if (lo.connection instanceof StatefulRedisClusterConnection) {
                lo.reactiveCommands = ((StatefulRedisClusterConnection) lo.connection).reactive();
            } else if (lo.connection instanceof StatefulRedisSentinelConnection) {
                lo.reactiveCommands = ((StatefulRedisSentinelConnection) lo.connection).reactive();
            } else {
                throw new CacheConfigException("type " + lo.connection.getClass() + " is not supported");
            }
        }
        return lo.reactiveCommands;
    }

    public void removeAndClose(AbstractRedisClient redisClient) {
        LettuceObjects lo = (LettuceObjects) map.remove(redisClient);
        if (lo == null) {
            return;
        }
        if (lo.connection != null) {
            lo.connection.close();
        }
        redisClient.shutdown();
    }


}
