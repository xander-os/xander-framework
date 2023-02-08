package com.macro.xander.common.api;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/3 11:45
 * @email ：zhrunxin33@gmail.com
 * @description：封装API错误码
 */
public interface IErrorCode {
    /**
     * 获取API返回码
     * @return
     */
    long getCode();

    /**
     * 获取信息
     * @return
     */
    String getMessage();
}
