package com.chinamobile.springboot.exception;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 23:25
 */
public class CacheConfigException extends CacheException {

    private static final long serialVersionUID = 407363218789422751L;

    public CacheConfigException(Throwable cause) {
        super(cause);
    }

    public CacheConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheConfigException(String message) {
        super(message);
    }
}