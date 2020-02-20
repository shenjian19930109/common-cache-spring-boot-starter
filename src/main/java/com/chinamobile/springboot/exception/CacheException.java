package com.chinamobile.springboot.exception;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 22:56
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = 6784591779395636834L;

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}
