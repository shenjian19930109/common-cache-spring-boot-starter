package com.chinamobile.springboot.exception;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 22:55
 */
public class CacheEncodeException extends CacheException {

    private static final long serialVersionUID = -1470638098248055917L;

    public CacheEncodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
