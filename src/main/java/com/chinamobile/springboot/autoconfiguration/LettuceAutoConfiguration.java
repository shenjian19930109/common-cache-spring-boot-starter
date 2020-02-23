package com.chinamobile.springboot.autoconfiguration;

import com.chinamobile.springboot.api.sync.SyncRedisLettuceCache;
import com.chinamobile.springboot.lettuce.RedisLettuceCacheConfig;
import com.chinamobile.springboot.properties.LettuceClusterProperties;
import com.chinamobile.springboot.properties.LettuceProperties;
import com.chinamobile.springboot.properties.LettuceSingleProperties;
import com.chinamobile.springboot.support.DefaultCacheCodec;
import com.chinamobile.springboot.support.FastjsonKeyConvertor;
import com.chinamobile.springboot.support.JavaValueDecoder;
import com.chinamobile.springboot.support.JavaValueEncoder;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/13 17:22
 */
@Configuration
@ConditionalOnClass(RedisURI.class)
@EnableConfigurationProperties(LettuceProperties.class)
public class LettuceAutoConfiguration {

    @Autowired
    private LettuceProperties lettuceProperties;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(ClientResources.class)
    public ClientResources clientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    @ConditionalOnProperty(name = "commoncache.redis.lettuce.single.host")
    public RedisURI singleRedisUri() {

        LettuceSingleProperties singleProperties = lettuceProperties.getSingle();
        LettuceAssert.notEmpty(singleProperties.getHost(), "host must not be empty");
        LettuceAssert.notNull(singleProperties.getPort(), "port must not be null");
        RedisURI.Builder builder = RedisURI.builder()
                .withHost(singleProperties.getHost())
                .withPort(singleProperties.getPort());
        return StringUtils.isBlank(singleProperties.getPassword())
                ? builder.build() : builder.withPassword(singleProperties.getPassword()).build();
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(name = "commoncache.redis.lettuce.single.host")
    public RedisClient singleRedisClient(ClientResources clientResources, @Qualifier("singleRedisUri") RedisURI singleRedisUri) {
        return RedisClient.create(clientResources, singleRedisUri);
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(name = "commoncache.redis.lettuce.single.host")
    public StatefulRedisConnection<String, String> singleRedisConnection(@Qualifier("singleRedisClient") RedisClient singleRedisClient) {
        return singleRedisClient.connect(new DefaultCacheCodec());
    }

    @Bean
    @ConditionalOnProperty(name = "commoncache.redis.lettuce.single.host")
    public RedisLettuceCacheConfig singleRedisLettuceCacheConfig(@Qualifier("singleRedisClient") AbstractRedisClient redisClient,
                                                                 @Qualifier("singleRedisConnection") StatefulConnection connection) {
        return new RedisLettuceCacheConfig(new FastjsonKeyConvertor(), new JavaValueEncoder(true),
                new JavaValueDecoder(true), redisClient, connection);
    }

    @Bean
    @ConditionalOnProperty(prefix = "commoncache.redis.lettuce.single.api", value = "type", havingValue = "sync")
    public SyncRedisLettuceCache syncSingleRedisLettuceCache(@Qualifier("singleRedisLettuceCacheConfig") RedisLettuceCacheConfig config) {
        return new SyncRedisLettuceCache(config);
    }



//    @Bean
//    @ConditionalOnMissingBean(name = "singleRedisCommands")
//    @ConditionalOnProperty(prefix = "lettuce.single.api", value = "type", havingValue = "sync")
//    public RedisCommands<String, String> singleRedisCommands(@Qualifier("singleRedisConnection") StatefulRedisConnection<String, String> singleRedisConnection) {
//        return singleRedisConnection.sync();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(name = "singleRedisAsyncCommands")
//    @ConditionalOnProperty(prefix = "lettuce.single.api", value = "type", havingValue = "async")
//    public RedisAsyncCommands<String, String> singleRedisAsyncCommands(@Qualifier("singleRedisConnection") StatefulRedisConnection<String, String> singleRedisConnection) {
//        return singleRedisConnection.async();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(name = "singleRedisReactiveCommands")
//    @ConditionalOnProperty(prefix = "lettuce.single.api", value = "type", havingValue = "reactive")
//    public RedisReactiveCommands<String, String> singleRedisReactiveCommands(@Qualifier("singleRedisConnection") StatefulRedisConnection<String, String> singleRedisConnection) {
//        return singleRedisConnection.reactive();
//    }

    @Bean
    @ConditionalOnProperty(name = "commoncache.redis.lettuce.cluster.nodes")
    public List<RedisURI> clusterRedisUris() {
        LettuceClusterProperties clusterProperties = lettuceProperties.getCluster();
        String nodes = clusterProperties.getNodes();
        LettuceAssert.notEmpty(nodes, "nodes must not be empty");
        String[] split = nodes.split(",");
        List<RedisURI> redisURIs = new ArrayList<>();
        for (String node : split) {
            String[] hostAndPort = node.trim().split(":");
            if (hostAndPort == null || hostAndPort.length != 2) {
                throw new RuntimeException("invalid nodes");
            }
            RedisURI.Builder builder = RedisURI.builder()
                    .withHost(hostAndPort[0].trim())
                    .withPort(Integer.valueOf(hostAndPort[1].trim()));
            RedisURI redisURI = StringUtils.isBlank(clusterProperties.getPassword())
                    ? builder.build(): builder.withPassword(clusterProperties.getPassword()).build();
            redisURIs.add(redisURI);
        }
        return redisURIs;
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(name = "commoncache.redis.lettuce.cluster.nodes")
    public RedisClusterClient redisClusterClient(ClientResources clientResources, @Qualifier("clusterRedisUris") List<RedisURI> clusterRedisUris) {
        return RedisClusterClient.create(clientResources, clusterRedisUris);
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(name = "commoncache.redis.lettuce.cluster.nodes")
    public StatefulRedisClusterConnection<String, String> redisClusterConnection(RedisClusterClient redisClusterClient) {
        return redisClusterClient.connect(new DefaultCacheCodec());
    }

    @Bean
    @ConditionalOnProperty(name = "commoncache.redis.lettuce.cluster.nodes")
    public RedisLettuceCacheConfig clusterRedisLettuceCacheConfig(@Qualifier("redisClusterClient") AbstractRedisClient redisClient,
                                                                  @Qualifier("redisClusterConnection") StatefulConnection connection) {
        return new RedisLettuceCacheConfig(new FastjsonKeyConvertor(), new JavaValueEncoder(true),
                new JavaValueDecoder(true), redisClient, connection);
    }

    @Bean
    @ConditionalOnProperty(prefix = "commoncache.redis.lettuce.cluster.api", value = "type", havingValue = "sync")
    public SyncRedisLettuceCache syncClusterRedisLettuceCache(@Qualifier("clusterRedisLettuceCacheConfig") RedisLettuceCacheConfig config) {
        return new SyncRedisLettuceCache(config);
    }



//    @Bean
//    @ConditionalOnMissingBean(name = "redisClusterCommands")
//    @ConditionalOnProperty(prefix = "lettuce.cluster.api", value = "type", havingValue = "sync")
//    public RedisAdvancedClusterCommands<String, String> redisClusterCommands(@Qualifier("redisClusterConnection") StatefulRedisClusterConnection<String, String> redisClusterConnection) {
//        return redisClusterConnection.sync();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(name = "redisClusterAsyncCommands")
//    @ConditionalOnProperty(prefix = "lettuce.cluster.api", value = "type", havingValue = "async")
//    public RedisAdvancedClusterAsyncCommands<String, String> redisClusterAsyncCommands(@Qualifier("redisClusterConnection") StatefulRedisClusterConnection<String, String> redisClusterConnection) {
//        return redisClusterConnection.async();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(name = "redisClusterReactiveCommands")
//    @ConditionalOnProperty(prefix = "lettuce.cluster.api", value = "type", havingValue = "reactive")
//    public RedisAdvancedClusterReactiveCommands<String, String> redisClusterReactiveCommands(@Qualifier("redisClusterConnection") StatefulRedisClusterConnection<String, String> redisClusterConnection) {
//        return redisClusterConnection.reactive();
//    }


}
