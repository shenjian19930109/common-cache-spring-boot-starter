package com.chinamobile.springboot.api.sync;

import com.chinamobile.springboot.lettuce.RedisLettuceCacheConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/23 16:05
 */
@Slf4j
public class SyncRedisLettuceCache extends RedisLettuceCache {

    public SyncRedisLettuceCache(RedisLettuceCacheConfig config) {
        super(config);
    }

//    @Override
//    protected CacheGetResult<String> do_GET(String key) {
//
//        try {
//            byte[] newKey = buildKey(key);
//            byte[] result = commands.get(newKey);
//            if (result == null) {
//                return NOT_EXISTS_WITHOUT_MSG;
//            }else {
//                String newResult = (String) valueDecoder.apply(result);
//                return new CacheGetResult<>(CacheResultCode.SUCCESS, null, newResult);
//            }
//        } catch (Exception e) {
//            log.error("do_GET : (" + key + "), exception : ", e);
//            return new CacheGetResult<>(e);
//        }
//    }

//    @Override
//    protected CacheResult do_SET(String key, String value, long expire, TimeUnit timeUnit) {
//
//        CacheResult cacheResult;
//        String result;
//        try {
//            byte[] newKey = buildKey(key);
//            if (expire <= 0 || timeUnit == null) {
//                result = commands.set(newKey, valueEncoder.apply(value));
//            }else {
//                result = commands.psetex(newKey, timeUnit.toMillis(expire), valueEncoder.apply(value));
//            }
//            if (result.equalsIgnoreCase("OK")) {
//                cacheResult = SUCCESS_WITHOUT_MSG;
//            }else {
//                cacheResult = new CacheResult(CacheResultCode.FAIL, result);
//            }
//            return cacheResult;
//        }catch (Exception e) {
//            log.error("do_SET : (" + key + ", " + value + "), exception : ", e);
//            return new CacheResult(e);
//        }
//    }

//    @Override
//    protected CacheResult do_SET_IF_NOT_EXIST(String key, String value, long expire, TimeUnit timeUnit) {
//
//        CacheResult cacheResult;
//        String result;
//        try {
//            byte[] newKey = buildKey(key);
//            if (expire <= 0 || timeUnit == null) {
//                result = commands.set(newKey, valueEncoder.apply(value), SetArgs.Builder.nx());
//            }else {
//                result = commands.set(newKey, valueEncoder.apply(value), SetArgs.Builder.nx().px(timeUnit.toMillis(expire)));
//            }
//            if (result == null) {
//                cacheResult = ALREADY_EXISTS_WITHOUT_SET;
//            }else if (result.equalsIgnoreCase("OK")) {
//                cacheResult = SUCCESS_WITHOUT_MSG;
//            }else {
//                cacheResult = new CacheResult(CacheResultCode.FAIL, result);
//            }
//            return cacheResult;
//        }catch (Exception e) {
//            log.error("do_SET_IF_NOT_EXIST : (" + key + ", " + value + "), exception : ", e);
//            return new CacheResult(e);
//        }
//    }

//    @Override
//    protected CacheResult do_SET_IF_EXIST(String key, String value, long expire, TimeUnit timeUnit) {
//
//        CacheResult cacheResult;
//        String result;
//        try {
//            byte[] newKey = buildKey(key);
//            if (expire <= 0 || timeUnit == null) {
//                result = commands.set(newKey, valueEncoder.apply(value), SetArgs.Builder.xx());
//            }else {
//                result = commands.set(newKey, valueEncoder.apply(value), SetArgs.Builder.xx().px(timeUnit.toMillis(expire)));
//            }
//            if (result == null) {
//                cacheResult = NOT_EXISTS_WITHOUT_SET;
//            }else if (result.equalsIgnoreCase("OK")) {
//                cacheResult = SUCCESS_WITHOUT_MSG;
//            }else {
//                cacheResult = new CacheResult(CacheResultCode.FAIL, result);
//            }
//            return cacheResult;
//        }catch (Exception e) {
//            log.error("do_SET_IF_EXIST : (" + key + ", " + value + "), exception : ", e);
//            return new CacheResult(e);
//        }
//    }

//    @Override
//    protected CacheResult do_DELETE(String key) {
//
//        CacheResult cacheResult;
//        try {
//            byte[] newKey = buildKey(key);
//            Long result = keyCommands.del(newKey);
//            if (result == 1) {
//                cacheResult = SUCCESS_WITHOUT_MSG;
//            }else if (result == 0) {
//                cacheResult = NOT_EXISTS_WITHOUT_MSG;
//            }else {
//                cacheResult = FAIL_WITHOUT_MSG;
//            }
//            return cacheResult;
//        }catch (Exception e) {
//            log.error("do_DELETE : (" + key + "), exception : ", e);
//            return new CacheResult(e);
//        }
//    }
}
