package com.macro.xander.auth.exception;

import com.macro.xander.common.api.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/6 21:24
 * @email ：zhrunxin33@gmail.com
 * @description：全局处理Oauth2抛出异常
 */

@ControllerAdvice
public class Oauth2ExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public CommonResult handleOauth2(Exception e){
        return CommonResult.failed(e.getMessage());
    }
}
