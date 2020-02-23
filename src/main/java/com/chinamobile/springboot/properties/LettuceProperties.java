package com.chinamobile.springboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/13 17:17
 */
@Data
@ConfigurationProperties(prefix = "commoncache.redis.lettuce")
public class LettuceProperties {

    private LettuceSingleProperties single;
    private LettuceClusterProperties cluster;
}
