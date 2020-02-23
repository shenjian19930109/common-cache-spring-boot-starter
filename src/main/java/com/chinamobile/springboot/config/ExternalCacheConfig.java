package com.chinamobile.springboot.config;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 22:45
 */
@Getter
@Setter
public class ExternalCacheConfig extends CacheConfig {

    private String keyPrefix;

    private Function<Object, byte[]> valueEncoder;

    private Function<byte[], Object> valueDecoder;

    public ExternalCacheConfig() {
    }

    public ExternalCacheConfig(Function<Object, byte[]> valueEncoder, Function<byte[], Object> valueDecoder) {
        this.valueEncoder = valueEncoder;
        this.valueDecoder = valueDecoder;
    }

    public ExternalCacheConfig(Function<Object, Object> keyConvertor, Function<Object, byte[]> valueEncoder, Function<byte[], Object> valueDecoder) {
        super(keyConvertor);
        this.valueEncoder = valueEncoder;
        this.valueDecoder = valueDecoder;
    }


}
