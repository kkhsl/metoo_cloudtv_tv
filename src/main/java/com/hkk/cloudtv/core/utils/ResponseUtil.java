package com.hkk.cloudtv.core.utils;

import com.hkk.cloudtv.vo.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Title: ResponseUtil.java
 * </p>
 *
 * <p>
 *     Description: 异常捕获结果处理工具类
 * </p>
 *
 * <author>
 *     HKK
 * </author>
 *
 */
public class ResponseUtil {

    public static Object unlogin() {
        return result(401, "Log in");
    }

    public static Object unauthz() {
        return result(403, "Insufficient authority");
    }

    public static Object badArgumentValue() {
        return result(402, "Parameter error");
    }

    public static Object nullPointException() {
        return result(402, "Parameter is null");
    }

    public static Object arithmeticException(){
        return result(402,"parameter error,Abnormal operation");
    }

    public static Object serious(){
        return result(502,"System error");
    }

    public static Object httpRequestMethodNotSupportedException(){
        return new Result(405, "Method Not Allowed");
    }

    public static Object fail(int errno, String errmsg) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("errno", errno);
        obj.put("errmsg", errmsg);
        return obj;
    }

    public static Object result(int errno, String errmsg) {
        Result obj = new Result(errno,errmsg);
        return obj;
    }
}
