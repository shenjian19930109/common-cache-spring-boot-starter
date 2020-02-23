package com.chinamobile.springboot.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 22:28
 */
@Getter
@Setter
@NoArgsConstructor
public class CacheConfig implements Cloneable {

    private long defaultExpireMillis = Integer.MAX_VALUE * 1000;

    private Function<Object, Object> keyConvertor;

    public CacheConfig(Function<Object, Object> keyConvertor) {
        this.keyConvertor = keyConvertor;
    }
}
