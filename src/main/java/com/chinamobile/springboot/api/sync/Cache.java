package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.result.CacheGetResult;
import com.chinamobile.springboot.result.CacheResult;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/17 15:49
 */
public interface Cache extends Cloneable {

    //-----------------------------JSR 107 style API------------------------------------------------

    void set(String key, String value);

    String get(String key);

    Long del(String key);

    void setex(String key, String value, int seconds);

    void setpx(String key, String value, long timeout);

    boolean setnx(String key, String value);

    boolean setxx(String key, String value);

    void delAll(Set<String> keys);

    CacheGetResult<String> GET(String key);

    CacheResult SET(String key, String value);

    CacheResult SET(String key, String value, long expire, TimeUnit timeUnit);

    default String computeIfAbsent(String key, Function<String, String> loader) {
        return computeIfAbsent(key, loader, false);
    }

    String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull);

    String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull, long expire, TimeUnit timeUnit);


}
