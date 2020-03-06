package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.config.CacheConfig;
import com.chinamobile.springboot.result.CacheGetResult;
import com.chinamobile.springboot.result.CacheResult;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/17 15:49
 */
public interface Cache extends Cloneable {

    //-----------------------------JSR 107 style API------------------------------------------------

    default void set(String key, String value) {
        SET(key, value);
    }

    default void set(String key, String value, long expire, TimeUnit timeUnit) {
        SET(key, value, expire, timeUnit);
    }

    default boolean setIfNotExist(String key, String value) {
        CacheResult result = SET_IF_NOT_EXIST(key, value);
        return result.isSuccess();
    }

    default boolean setIfNotExist(String key, String value, long expire, TimeUnit timeUnit) {
        CacheResult result = SET_IF_NOT_EXIST(key, value, expire, timeUnit);
        return result.isSuccess();
    }

    default boolean setIfExist(String key, String value) {
        CacheResult result = SET_IF_EXIST(key, value, config().getDefaultExpireMillis(), TimeUnit.MILLISECONDS);
        return result.isSuccess();
    }

    default boolean setIfExist(String key, String value, long expire, TimeUnit timeUnit) {
        CacheResult result = SET_IF_EXIST(key, value, expire, timeUnit);
        return result.isSuccess();
    }

    default String get(String key) {
        CacheGetResult<String> result = GET(key);
        if (result.isSuccess()) {
            return result.getData();
        }else {
            return null;
        }
    }

    default boolean delete(String key) {
        return DELETE(key).isSuccess();
    }

//    void setex(String key, String value, int seconds);
//
//    void setpx(String key, String value, long timeout);
//
//    boolean setnx(String key, String value);
//
//    boolean setxx(String key, String value);
//
//    void delAll(Set<String> keys);

    CacheGetResult<String> GET(String key);

    default CacheResult SET(String key, String value) {
        if (key == null) {
            return CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }
        return SET(key, value, config().getDefaultExpireMillis(), TimeUnit.MILLISECONDS);
    }

    CacheResult SET(String key, String value, long expire, TimeUnit timeUnit);

    default CacheResult SET_IF_NOT_EXIST(String key, String value) {
        if (key == null) {
            return CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }
        return SET_IF_NOT_EXIST(key, value, config().getDefaultExpireMillis(), TimeUnit.MILLISECONDS);
    }

    CacheResult SET_IF_NOT_EXIST(String key, String value, long expire, TimeUnit timeUnit);

    default CacheResult SET_IF_EXIST(String key, String value) {
        if (key == null) {
            return CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }
        return SET_IF_EXIST(key, value, config().getDefaultExpireMillis(), TimeUnit.MILLISECONDS);
    }

    CacheResult SET_IF_EXIST(String key, String value, long expire, TimeUnit timeUnit);

    CacheResult DELETE(String key);

    default String computeIfAbsent(String key, Function<String, String> loader) {
        return computeIfAbsent(key, loader, false);
    }

    String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull);

    String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull, long expire, TimeUnit timeUnit);

    /********************************************************************/

    CacheConfig config();

}
