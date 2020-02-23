package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.config.ExternalCacheConfig;
import com.chinamobile.springboot.exception.CacheConfigException;
import com.chinamobile.springboot.exception.CacheException;
import com.chinamobile.springboot.util.CacheKeyConvertorUtil;

import java.io.IOException;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 22:07
 */
public abstract class AbstractExternalCache extends AbstractCache {

    private ExternalCacheConfig config;

    public AbstractExternalCache(ExternalCacheConfig config) {
        this.config = config;
        checkConfig();
    }

    protected void checkConfig() {
        if (config.getValueEncoder() == null) {
            throw new CacheConfigException("no value encoder");
        }
        if (config.getValueDecoder() == null) {
            throw new CacheConfigException("no value decoder");
        }
        // TODO 前缀
//        if (config.getKeyPrefix() == null){
//            throw new CacheConfigException("keyPrefix is required");
//        }
    }

    protected byte[] buildKey(Object key) {
        try {
            Object newKey = key;
            if (key instanceof byte[] || key instanceof String) {
                newKey = key;
            } else if (config.getKeyConvertor() != null) {
                newKey = config.getKeyConvertor().apply(key);
            }
            // TODO 前缀
            return CacheKeyConvertorUtil.convertObj2ByteArr(newKey);
        } catch (IOException e) {
            throw new CacheException(e);
        }
    }

}
