//package com.chinamobile.springboot.common.base;
//
//import com.chinamobile.springboot.common.enums.ResultCodeEnum;
//import com.chinamobile.springboot.result.ResultObj;
//import org.apache.commons.lang3.StringUtils;
//
///**
// * @description:
// * @author: shenjian
// * @create: 2020/2/17 17:00
// */
//public interface BaseResultInterface {
//    /**info message正则表达式**/
//    public static final String OK_CODE_REGEX = "^[0-9]+I$";
//    /**error message正则表达式**/
//    public static final String ERROR_CODE_REGEX = "^[0-9]+E$";
//
//    /**
//     * 成功返回方法
//     *
//     * @param <T> 泛型
//     * @return 统一返回值
//     */
//    default <T> ResultObj<T> success() {
//        return success(ResultCodeEnum.success.getCode(), null);
//    }
//
//    /**
//     * 成功返回方法
//     *
//     * @param <T>  泛型
//     * @param data data
//     * @return 统一返回值
//     */
//    default <T> ResultObj<T> success(T data) {
//        return success(ResultCodeEnum.success.getCode(), data);
//    }
//
//    /**
//     * 成功返回方法
//     *
//     * @param data data
//     * @param code 结果code
//     * @param <T>  泛型
//     * @return 统一返回值
//     */
//    default <T> ResultObj<T> success(String code, T data) {
//        return success(code, null, data);
//    }
//
//    /**
//     * 成功返回方法
//     *
//     * @param code  结果code
//     * @param <T>   泛型
//     * @param data  data
//     * @param msg 描述参数
//     * @return 统一返回值
//     */
//    default <T> ResultObj<T> success(String code, String msg, T data) {
//        if (!code.matches(OK_CODE_REGEX)) {
//            throw new IllegalArgumentException("code must end with 'I'");
//        }
//        ResultObj<T> resultObj = generateResultObj(code, msg, data);
//        return resultObj;
//    }
//
//    /**
//     * 失败返回方法
//     *
//     * @param code 结果code
//     * @param <T>  泛型
//     * @return 统一返回值
//     */
//    default <T> ResultObj<T> fail(String code) {
//        return fail(code, null, null);
//    }
//
//    /**
//     * 失败返回方法
//     *
//     * @param data data
//     * @param code 结果code
//     * @param <T>  泛型
//     * @return 统一返回值
//     */
//    default <T> ResultObj<T> fail(String code, T data) {
//        return fail(code, null, data);
//    }
//
//    /**
//     * 失败返回方法
//     *
//     * @param code  结果code
//     * @param <T>   泛型
//     * @param data  data
//     * @param msg 描述参数
//     * @return 统一返回值
//     */
//    default <T> ResultObj<T> fail(String code, String msg, T data) {
//        if (!code.matches(ERROR_CODE_REGEX)) {
//            throw new IllegalArgumentException("code must end with 'E'");
//        }
//        ResultObj<T> resultObj = generateResultObj(code, msg, data);
//        return resultObj;
//    }
//
//    /**
//     * 组装消息返回对象
//     * @param code  结果code
//     * @param <T>   泛型
//     * @param data  data
//     * @param msg 描述参数
//     * @return 统一返回值
//     */
//    default <T> ResultObj<T> generateResultObj(String code, String msg, T data) {
//        ResultObj<T> resultObj = new ResultObj<>();
//        resultObj.setCode(code);
//        resultObj.setMsg(msg);
//        resultObj.setData(data);
//        return resultObj;
//    }
//
//    /**
//     * 判断是否成功
//     *
//     * @param ro ro
//     * @return 成功或失败
//     */
//    default boolean isSuccess(ResultObj ro) {
//        if (ro != null) {
//            String code = ro.getCode();
//            if (!StringUtils.isEmpty(code)) {
//                if (code.matches(OK_CODE_REGEX)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//}
