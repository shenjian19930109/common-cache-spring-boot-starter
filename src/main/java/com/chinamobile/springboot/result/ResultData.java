package com.chinamobile.springboot.result;

import com.chinamobile.springboot.common.enums.CacheResultCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/18 23:31
 */
@Getter
@Setter
public class ResultData {

    private CacheResultCode resultCode;
    private String message;
    private Object data;

    public ResultData(Throwable e) {
        this.resultCode = CacheResultCode.FAIL;
        this.message = "Ex : " + e.getClass() + ", " + e.getMessage();
    }

    public ResultData(CacheResultCode resultCode, String message, Object data) {
        this.resultCode = resultCode;
        this.message = message;
        this.data = data;
    }
}
