package com.chinamobile.springboot.result;

import com.chinamobile.springboot.common.enums.CacheResultCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/18 23:24
 */
@Getter
@Setter
public class CacheResult {

    public static final String MSG_ILLEGAL_ARGUMENT = "illegal argument";
    public static final String MSG_ILLEGAL_EXPIRE = "illegal expire";
    public static final String MSG_ILLEGAL_TIMEUNIT = "illegal timeunit";

    private static Duration DEFAULT_TIMEOUT = Duration.ofMillis(1000);

    public static final CacheResult SUCCESS_WITHOUT_MSG = new CacheResult(CacheResultCode.SUCCESS, null);
//    public static final CacheResult PART_SUCCESS_WITHOUT_MSG = new CacheResult(CacheResultCode.PART_SUCCESS, null);
    public static final CacheResult FAIL_WITHOUT_MSG = new CacheResult(CacheResultCode.FAIL, null);
    public static final CacheResult FAIL_ILLEGAL_ARGUMENT = new CacheResult(CacheResultCode.FAIL, MSG_ILLEGAL_ARGUMENT);
    public static final CacheResult FAIL_ILLEGAL_EXPIRE = new CacheResult(CacheResultCode.FAIL, MSG_ILLEGAL_EXPIRE);
    public static final CacheResult FAIL_ILLEGAL_TIMEUNIT = new CacheResult(CacheResultCode.FAIL, MSG_ILLEGAL_TIMEUNIT);
//    public static final CacheResult ALREADY_EXISTS_WITHOUT_SET = new CacheResult(CacheResultCode.ALREADY_EXISTS, null);
//    public static final CacheResult NOT_EXISTS_WITHOUT_SET = new CacheResult(CacheResultCode.NOT_EXISTS, null);

    private CacheResultCode resultCode;
    private String message;
    private CompletionStage<ResultData> future;

    private Duration timeout = DEFAULT_TIMEOUT;

    public CacheResult(CacheResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public CacheResult(CompletionStage<ResultData> future) {
        this.future = future;
    }

    public CacheResult(Throwable e) {
        this.resultCode = CacheResultCode.FAIL;
        this.message = "Ex : " + e.getClass() + ", " + e.getMessage();
    }

    public boolean isSuccess() {
        return getResultCode() == CacheResultCode.SUCCESS;
    }

    protected void waitForResult() {
        waitForResult(timeout);
    }

    protected void waitForResult(Duration timeout) {
        if (resultCode != null) {
            return;
        }
        try {
            ResultData resultData = future.toCompletableFuture().get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            settingSuccessResult(resultData);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            settingFailResult(e);
        }
    }

    protected void settingFailResult(Exception e) {
        this.resultCode = CacheResultCode.FAIL;
        this.message = e.getClass() + ":" + e.getMessage();
    }

    protected void settingSuccessResult(ResultData resultData) {
        this.resultCode = resultData.getResultCode();
        this.message = resultData.getMessage();
    }

    public CacheResultCode getResultCode() {
        waitForResult();
        return resultCode;
    }
}
