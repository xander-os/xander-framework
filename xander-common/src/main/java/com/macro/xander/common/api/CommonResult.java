package com.macro.xander.common.api;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/3 11:39
 * @email ：zhrunxin33@gmail.com
 * @description：统一返回对象
 */
public class CommonResult<T> {
    private long code;
    private String message;
    private T data;

    public CommonResult() {
    }

    public CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     * @param data 获取数据
     * @return
     */
    public static <T> CommonResult<T> success(T data){
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果，并自定义返回信息
     * @param data
     * @param message
     * @return
     */
    public static <T> CommonResult<T> success(T data, String message){
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     * @param errorCode
     * @return
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode){
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果，并自定义返回信息
     * @param errorCode
     * @param message
     * @return
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode, String message){
        return new CommonResult<T>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     * @return
     */
    public static <T> CommonResult<T> failed(){
        return new CommonResult<T>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), null);
    }

    /**
     * 失败返回结果，并自定义返回信息
     * @param message
     * @return
     */
    public static <T> CommonResult<T> failed(String message){
        return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 参数校验失败
     * @param message
     * @return
     */
    public static <T> CommonResult<T> validateFailed(String message){
        return new CommonResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     * @param data
     * @return
     */
    public static <T> CommonResult<T> unauthorized(T data){
        return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     * @param data
     * @return
     */
    public static <T> CommonResult<T> forbidden(T data){
        return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }


    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
