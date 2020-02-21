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

    /**
     * set方法
     *
     * @param key 键
     * @param value 值
     * */
    void set(String key, String value);

    /**
     * get方法
     *
     * @param key 键
     * @return value
     * */
    String get(String key);

    /**
     * del方法
     *
     * @param key
     * @return 删除个数
     * */
    Long del(String key);

    /**
     * 设置过期时间的set方法（单位：秒）
     *
     * @param key 键
     * @param value 值
     * @param seconds 过期时间
     * */
    void setex(String key, String value, int seconds);

    /**
     * 设置过期时间的set方法（单位：毫秒）
     *
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * */
    void setpx(String key, String value, long timeout);

    /**
     * 如果不存在某个key时，执行set
     *
     * @param key 键
     * @param value 值
     * @return true：set成功；false：set失败
     * */
    boolean setnx(String key, String value);

    /**
     * 如果存在某个key时，执行set
     *
     * @param key 键
     * @param value 值
     * @return true: set成功；false：set失败
     * */
    boolean setxx(String key, String value);

    /**
     * 删除所有集合中的key对应的键值对
     *
     * @param keys 键的集合
     * */
    void delAll(Set<String> keys);

    /**
     * 带返回状态封装的get方法
     *
     * @param key 键
     * @return CacheGetResult
     * @see CacheGetResult
     * */
    CacheGetResult<String> GET(String key);

    /**
     * 带返回状态封装的set方法
     *
     * @param key 键
     * @param value 值
     * @return CacheResult
     * @see CacheResult
     * */
    CacheResult SET(String key, String value);

    /**
     * 带返回状态封装,且包含过期时间的set方法
     *
     * @param key 键
     * @param value 值
     * @param expire 过期时间
     * @param timeUnit 时间单位
     * @return CacheResult
     * @see CacheResult
     * */
    CacheResult SET(String key, String value, long expire, TimeUnit timeUnit);

    /**
     * 特殊get方法：当缓存中key不存在时执行指定方法，并将结果缓存
     *              当缓存中key存在时，直接返回value
     * @param key 键
     * @param loader 值
     * @return value
     * */
    default String computeIfAbsent(String key, Function<String, String> loader) {
        return computeIfAbsent(key, loader, false);
    }

    /**
     * 特殊get方法：当缓存中key不存在时执行指定方法，并将结果缓存
     *              当缓存中key存在时，直接返回value
     *              可以指定当执行指定方法返回null时，是否将null缓存
     * @param key 键
     * @param loader 值
     * @param cacheNullWhenLoaderReturnNull 当执行指定方法返回null时，是否将null缓存
     * @return value
     * */
    String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull);

    /**
     * 特殊get方法：当缓存中key不存在时执行指定方法，并将结果缓存
     *              当缓存中key存在时，直接返回value
     *              可以指定当执行指定方法返回null时，是否将null缓存
     *              可以加超时时间
     * @param key 键
     * @param loader 值
     * @param cacheNullWhenLoaderReturnNull 当执行指定方法返回null时，是否将null缓存
     * @param expire 过期时间
     * @param timeUnit 时间单位
     * @return value
     * */
    String computeIfAbsent(String key, Function<String, String> loader, boolean cacheNullWhenLoaderReturnNull, long expire, TimeUnit timeUnit);


}
