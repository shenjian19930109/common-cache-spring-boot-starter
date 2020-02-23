package com.chinamobile.springboot.support;

import com.alibaba.fastjson.JSON;

import java.util.function.Function;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/22 17:06
 */
public class FastjsonKeyConvertor implements Function<Object, Object> {

    public static final FastjsonKeyConvertor INSTANCE = new FastjsonKeyConvertor();

    /**
     * 若为byte[]/String 则直接返回，否则转化为json串返回
     * */
    @Override
    public Object apply(Object o) {
        if (o == null || o instanceof String || o instanceof byte[]) {
            return o;
        }
        return JSON.toJSONString(o);
    }
}
