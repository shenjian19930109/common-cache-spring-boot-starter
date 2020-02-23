package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.common.enums.CacheResultCode;
import com.chinamobile.springboot.result.CacheGetResult;
import com.chinamobile.springboot.result.CacheResult;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/17 16:24
 */
public abstract class AbstractCache implements Cache {

    @Override
    public final String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull) {
        return computeIfAbsent(key, loader, cacheNullWhenLoaderReturnNull, 0, null);
    }

    @Override
    public final String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull, long expire, TimeUnit timeUnit) {
        return computeIfAbsentImpl(key, loader, cacheNullWhenLoaderReturnNull, expire, timeUnit, this);
    }

    static String computeIfAbsentImpl(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull, long expire, TimeUnit timeUnit, Cache cache) {

        CacheGetResult<String> result = cache.GET(key);

        if (result.isSuccess() || result.getResultCode() == CacheResultCode.EXISTS_WITH_EMPTY_VALUE) {
            return result.getData();
        }else if (result.getResultCode() == CacheResultCode.NOT_EXISTS) {
            String dbValue = loader.apply(key);
            if (needUpdate(dbValue, cacheNullWhenLoaderReturnNull)) {
                if (expire <= 0 || timeUnit == null) {
                    cache.set(key, dbValue);
                }else {
                    cache.SET(key, dbValue, expire, timeUnit);
                }
            }
            return dbValue;
        }else {
            throw new RuntimeException("get : (" + key + ") fail!" );
        }
    }

    private static boolean needUpdate(String dbValue, boolean cacheNullWhenLoaderReturnNull) {
        if (StringUtils.isEmpty(dbValue) && cacheNullWhenLoaderReturnNull) {
            return true;
        }else if (StringUtils.isNotEmpty(dbValue)) {
            return true;
        }
        return false;
    }

    @Override
    public CacheGetResult<String> GET(String key) {

        long t = System.currentTimeMillis();
        CacheGetResult<String> result;
        if (key == null) {
            result = new CacheGetResult<>(CacheResultCode.FAIL, CacheResult.MSG_ILLEGAL_ARGUMENT, null);
        }else {
            result = do_GET(key);
        }
        return result;
    }

    protected abstract CacheGetResult<String> do_GET(String key);

    @Override
    public CacheResult SET(String key, String value, long expire, TimeUnit timeUnit) {

        long t = System.currentTimeMillis();
        CacheResult result;
        if (key == null) {
            result = CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }else {
            result = do_SET(key, value, expire, timeUnit);
        }
        return result;
    }

    protected abstract CacheResult do_SET(String key, String value, long expire, TimeUnit timeUnit);

    @Override
    public CacheResult DELETE(String key) {

        long t = System.currentTimeMillis();
        CacheResult result;
        if (key == null) {
            result = CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }else {
            result = do_DELETE(key);
        }
        return result;
    }

    protected abstract CacheResult do_DELETE(String key);

}
