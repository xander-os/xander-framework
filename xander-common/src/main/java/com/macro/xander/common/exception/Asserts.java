package com.macro.xander.common.exception;

import com.macro.xander.common.api.IErrorCode;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/4 10:38
 * @email ：zhrunxin33@gmail.com
 * @description：断言处理类，用于抛出各种API异常
 */
public class Asserts {

    public static void failed(String message){
        throw new ApiException(message);
    }

    public static void failed(IErrorCode errorCode){
        throw new ApiException(errorCode);
    }
}
