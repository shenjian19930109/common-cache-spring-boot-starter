package com.chinamobile.springboot.result;

import com.chinamobile.springboot.common.enums.CacheResultCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/18 23:24
 */
@Getter
@Setter
public class CacheResult {

    public static final String MSG_ILLEGAL_ARGUMENT = "illegal argument";

    private static Duration DEFAULT_TIMEOUT = Duration.ofMillis(1000);

    public static final CacheResult SUCCESS_WITHOUT_MSG = new CacheResult(CacheResultCode.SUCCESS, null);
//    public static final CacheResult PART_SUCCESS_WITHOUT_MSG = new CacheResult(CacheResultCode.PART_SUCCESS, null);
    public static final CacheResult FAIL_WITHOUT_MSG = new CacheResult(CacheResultCode.FAIL, null);
    public static final CacheResult FAIL_ILLEGAL_ARGUMENT = new CacheResult(CacheResultCode.FAIL, MSG_ILLEGAL_ARGUMENT);
//    public static final CacheResult EXISTS_WITHOUT_MSG = new CacheResult(CacheResultCode.EXISTS, null);

    /**
     * 返回码
     * */
    private CacheResultCode resultCode;
    /**
     * msg
     * */
    private String message;

    private Duration timeout = DEFAULT_TIMEOUT;

    public CacheResult(CacheResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public CacheResult(Throwable e) {
        this.resultCode = CacheResultCode.FAIL;
        this.message = "Ex : " + e.getClass() + ", " + e.getMessage();
    }

    public boolean isSuccess() {
        return getResultCode() == CacheResultCode.SUCCESS;
    }

}
