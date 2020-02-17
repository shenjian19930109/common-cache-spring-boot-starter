package com.chinamobile.springboot;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.reactive.RedisAdvancedClusterReactiveCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/13 21:17
 */
@Slf4j
@Component
public class RedisCommandLineRunner implements CommandLineRunner {

    @Autowired
    private StatefulRedisConnection<String, String> connection;

    @Autowired
    private StatefulRedisClusterConnection<String, String> clusterConnection;

    @Override
    public void run(String... args) throws Exception {
//        RedisCommands<String, String> redisCommands = connection.sync();
//        RedisAsyncCommands<String, String> async = connection.async();
//        RedisReactiveCommands<String, String> reactive = connection.reactive();
//        redisCommands.setex("name", 5, "throwable");
//        log.info("Get value:{}", redisCommands.get("name"));

        RedisAdvancedClusterCommands<String, String> sync = clusterConnection.sync();
        RedisAdvancedClusterAsyncCommands<String, String> async = clusterConnection.async();
        RedisAdvancedClusterReactiveCommands<String, String> reactive = clusterConnection.reactive();


    }
}
