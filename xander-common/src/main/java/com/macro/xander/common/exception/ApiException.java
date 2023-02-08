package com.macro.xander.common.exception;

import com.macro.xander.common.api.IErrorCode;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/4 10:32
 * @email ：zhrunxin33@gmail.com
 * @description：自定义异常（断言Assert和统一异常处理）
 */
public class ApiException extends RuntimeException{
    private IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message){
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
