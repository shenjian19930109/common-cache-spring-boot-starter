package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.common.enums.CacheResultCode;
import com.chinamobile.springboot.result.CacheGetResult;
import com.chinamobile.springboot.result.CacheResult;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.chinamobile.springboot.result.CacheResult.SUCCESS_WITHOUT_MSG;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/17 16:26
 */
@Slf4j
public class SingleCacheService extends AbstractExternalCache {

    private RedisCommands<String, String> singleRedisCommands;

    public SingleCacheService() {
    }

    public SingleCacheService(RedisCommands<String, String> singleRedisCommands) {
        this.singleRedisCommands = singleRedisCommands;
    }

    @Override
    public void set(String key, String value) {
        singleRedisCommands.set(key, value);
    }

    @Override
    public String get(String key) {
        return singleRedisCommands.get(key);
    }

    @Override
    public Long del(String key) {
        return singleRedisCommands.del(key);
    }

    @Override
    public void setex(String key, String value, int seconds) {
        singleRedisCommands.setex(key, seconds, value);
    }

    @Override
    public void setpx(String key, String value, long timeout) {
        SetArgs setArgs = SetArgs.Builder.px(timeout);
        singleRedisCommands.set(key, value, setArgs);
    }

    @Override
    public boolean setnx(String key, String value) {
        return singleRedisCommands.setnx(key, value);
    }

    @Override
    public boolean setxx(String key, String value) {
        SetArgs setArgs = SetArgs.Builder.xx();
        String result = singleRedisCommands.set(key, value, setArgs);
        return result.equalsIgnoreCase("OK") ? true : false;
    }

    @Override
    public void delAll(Set<String> keys) {
        String[] keysArr = new String[keys.size()];
        keys.toArray(keysArr);
        singleRedisCommands.del(keysArr);
    }

    @Override
    protected CacheGetResult<String> do_GET(String key) {
        try {
            String result = singleRedisCommands.get(key);
            if (StringUtils.isEmpty(result)) {
                Long isExist = singleRedisCommands.exists(key);
                return isExist == 1L ? new CacheGetResult<>(CacheResultCode.EXISTS_WITH_EMPTY_VALUE, null, result)
                        : new CacheGetResult<>(CacheResultCode.NOT_EXISTS, null, null);
            }
            return new CacheGetResult<>(CacheResultCode.SUCCESS, null, result);
        } catch (Exception e) {
            log.error("GET : (" + key + "), exception : ", e);
            return new CacheGetResult<>(CacheResultCode.FAIL, e.getMessage(), null);
        }
    }

    @Override
    protected CacheResult do_PUT(String key, String value, long expire, TimeUnit timeUnit) {

        try {
            CacheResult cacheResult;
            String result;
            if (expire <= 0 || timeUnit == null) {
                result = singleRedisCommands.set(key, value);
            }else {
                result = singleRedisCommands.psetex(key, timeUnit.toMillis(expire), value);
            }
            if (result.equalsIgnoreCase("OK")) {
                cacheResult = SUCCESS_WITHOUT_MSG;
            }else {
                cacheResult = new CacheResult(CacheResultCode.FAIL, result);
            }
            return cacheResult;
        }catch (Exception e) {
            log.error("PUT : (" + key + ", " + value + "), exception : ", e);
            return new CacheResult(e);
        }
    }
}
