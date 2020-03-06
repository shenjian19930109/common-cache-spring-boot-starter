package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.common.enums.CacheResultCode;
import com.chinamobile.springboot.result.CacheGetResult;
import com.chinamobile.springboot.result.CacheResult;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/17 16:24
 */
public abstract class AbstractCache implements Cache {

//    private CacheConfig config;
//
//    public AbstractCache(CacheConfig config) {
//        this.config = config;
//    }

    @Override
    public final String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull) {
        return computeIfAbsent(key, loader, cacheNullWhenLoaderReturnNull, 0, null);
    }

    @Override
    public final String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull,
                                        long expire, TimeUnit timeUnit) {
        return computeIfAbsentImpl(key, loader, cacheNullWhenLoaderReturnNull, expire, timeUnit, this);
    }

    static String computeIfAbsentImpl(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull,
                                      long expire, TimeUnit timeUnit, Cache cache) {

        CacheGetResult<String> result = cache.GET(key);

        if (result.isSuccess() /*|| result.getResultCode() == CacheResultCode.EXISTS_WITH_EMPTY_VALUE*/) {
            return result.getData();
        }else if (result.getResultCode() == CacheResultCode.NOT_EXISTS) {
            String dbValue = loader.apply(key);
            if (needUpdate(dbValue, cacheNullWhenLoaderReturnNull)) {
                if (expire <= 0 || timeUnit == null) {
                    cache.SET(key, dbValue);
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
        /*if (dbValue == null && cacheNullWhenLoaderReturnNull) {
            return true;
        }else if (dbValue != null) {
            return true;
        }
        return false;*/
        return dbValue != null || cacheNullWhenLoaderReturnNull;
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

    protected abstract CacheGetResult do_GET(String key);

    @Override
    public CacheResult SET(String key, String value, long expire, TimeUnit timeUnit) {

        long t = System.currentTimeMillis();
        CacheResult result;
        if (key == null) {
            result = CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }else {
            if (expire <= 0 && config().whenInValidExpireOrTimeUnitReturnFail()) {
                result = CacheResult.FAIL_ILLEGAL_EXPIRE;
            }else if (timeUnit == null && config().whenInValidExpireOrTimeUnitReturnFail()) {
                result = CacheResult.FAIL_ILLEGAL_TIMEUNIT;
            }else if (applyDefaultExpireAndTimeUnit(expire, timeUnit, config().whenInValidExpireOrTimeUnitReturnFail())) {
                result = do_SET(key, value, config().getDefaultExpireMillis(), TimeUnit.MILLISECONDS);
            }else {
                result = do_SET(key, value, expire, timeUnit);
            }
        }
        return result;
    }

    protected abstract CacheResult do_SET(String key, String value, long expire, TimeUnit timeUnit);

    @Override
    public CacheResult SET_IF_NOT_EXIST(String key, String value, long expire, TimeUnit timeUnit) {

        long t = System.currentTimeMillis();
        CacheResult result;
        if (key == null) {
            result = CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }else {
            if (expire <= 0 && config().whenInValidExpireOrTimeUnitReturnFail()) {
                result = CacheResult.FAIL_ILLEGAL_EXPIRE;
            }else if (timeUnit == null && config().whenInValidExpireOrTimeUnitReturnFail()) {
                result = CacheResult.FAIL_ILLEGAL_TIMEUNIT;
            }else if (applyDefaultExpireAndTimeUnit(expire, timeUnit, config().whenInValidExpireOrTimeUnitReturnFail())) {
                result = do_SET_IF_NOT_EXIST(key, value, config().getDefaultExpireMillis(), TimeUnit.MILLISECONDS);
            }else {
                result = do_SET_IF_NOT_EXIST(key, value, expire, timeUnit);
            }
        }
        return result;
    }

    protected abstract CacheResult do_SET_IF_NOT_EXIST(String key, String value, long expire, TimeUnit timeUnit);

    @Override
    public CacheResult SET_IF_EXIST(String key, String value, long expire, TimeUnit timeUnit) {

        long t = System.currentTimeMillis();
        CacheResult result;
        if (key == null) {
            result = CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }else {
            if (expire <= 0 && config().whenInValidExpireOrTimeUnitReturnFail()) {
                result = CacheResult.FAIL_ILLEGAL_EXPIRE;
            }else if (timeUnit == null && config().whenInValidExpireOrTimeUnitReturnFail()) {
                result = CacheResult.FAIL_ILLEGAL_TIMEUNIT;
            }else if (applyDefaultExpireAndTimeUnit(expire, timeUnit, config().whenInValidExpireOrTimeUnitReturnFail())) {
                result = do_SET_IF_EXIST(key, value, config().getDefaultExpireMillis(), TimeUnit.MILLISECONDS);
            }else {
                result = do_SET_IF_EXIST(key, value, expire, timeUnit);
            }
        }
        return result;
    }

    private boolean applyDefaultExpireAndTimeUnit(long expire, TimeUnit timeUnit, boolean inValidExpireOrTimeUnitReturnFail) {
        if (expire > 0 && timeUnit != null) {
            return false;
        }else if (!inValidExpireOrTimeUnitReturnFail) {
            return true;
        }
        return false;
    }

    protected abstract CacheResult do_SET_IF_EXIST(String key, String value, long expire, TimeUnit timeUnit);

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
