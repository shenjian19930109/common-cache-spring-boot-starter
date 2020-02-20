//package com.chinamobile.springboot.result;
//
//import lombok.Data;
//import org.apache.commons.lang3.StringUtils;
//
///**
// * @description:
// * @author: shenjian
// * @create: 2020/2/17 16:14
// */
//@Data
//public class ResultObj<T> {
//
//    /**
//     * 返回code
//     */
//    private String code;
//
//    /**
//     * 返回说明
//     */
//    private String msg;
//
//    /**
//     * 待包装的返回值
//     */
//    private T data;
//
//    /**
//     * 判断是否成功
//     *
//     * @return boolean
//     */
//    public boolean isSuccess() {
//        return StringUtils.isEmpty(code) ? false : code.endsWith("I");
//    }
//}
