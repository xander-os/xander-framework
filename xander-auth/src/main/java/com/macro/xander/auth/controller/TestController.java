package com.macro.xander.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/5 0:44
 * @email ：zhrunxin33@gmail.com
 * @description：
 */
@RestController
@Api(tags = "TestController", value = "测试Swagger类")
@RequestMapping("/oauth")
public class TestController {

    @ApiOperation("接口")
    @RequestMapping("/test")
    public String test(){
        return "66";
    }
}
