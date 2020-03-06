package com.chinamobile.springboot.result;

import com.chinamobile.springboot.common.enums.CacheResultCode;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CompletionStage;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/18 23:43
 */
@Getter
@Setter
public class CacheGetResult<T> extends CacheResult {

    private T data;

    public static final CacheGetResult NOT_EXISTS_WITHOUT_MSG = new CacheGetResult(CacheResultCode.NOT_EXISTS, null, null);
//    public static final CacheGetResult EXPIRED_WITHOUT_MSG = new CacheGetResult(CacheResultCode.EXPIRED, null ,null);

    public CacheGetResult(CacheResultCode resultCode, String message, T data) {
        super(resultCode, message);
        this.data = data;
    }

    public CacheGetResult(CompletionStage<ResultData> future) {
        super(future);
    }

    public CacheGetResult(Throwable e) {
        super(e);
    }

    public T getData() {
        waitForResult();
        return data;
    }

    @Override
    protected void settingSuccessResult(ResultData resultData) {
        super.settingSuccessResult(resultData);
        this.data = (T) resultData.getData();
    }

    @Override
    protected void settingFailResult(Exception e) {
        super.settingFailResult(e);
        this.data = null;
    }
}
