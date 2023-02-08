package com.macro.xander.common.exception;

import com.macro.xander.common.api.CommonResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/4 10:50
 * @email ：zhrunxin33@gmail.com
 * @description：全局异常处理（不向接口返回异常信息）
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截自定义异常，返回统一CommonResult对象
     */
    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResult handle(ApiException e){
        if(e.getErrorCode() != null){
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理逻辑一样的Exception可以放在一起拦截
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public CommonResult handleValidException(BindException e){
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }
}
