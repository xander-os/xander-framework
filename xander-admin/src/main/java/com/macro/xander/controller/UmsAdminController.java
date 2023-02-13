package com.macro.xander.controller;

import com.macro.xander.common.api.CommonResult;
import com.macro.xander.dto.TestValidatorParam;
import com.macro.xander.dto.UmsAdminLoginParam;
import com.macro.xander.dto.UmsAdminParam;
import com.macro.xander.model.UmsAdmin;
import com.macro.xander.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/12 12:00
 * @email ：zhrunxin33@gmail.com
 * @description：后台用户管理操作
 */
@RestController
@Api(tags = "UmsAdminController", value = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {

    @Autowired
    private UmsAdminService umsAdminService;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册")
    public CommonResult<UmsAdmin> register(@Validated @RequestBody UmsAdminParam umsAdminParam){
        UmsAdmin umsAdmin = umsAdminService.register(umsAdminParam);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation(value = "后台用户登陆")
    public CommonResult login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam){
        return umsAdminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
    }

    @RequestMapping(value = "testValidator", method = RequestMethod.POST)
    @ApiOperation(value = "测试参数")
    public CommonResult testValidator(@Validated @RequestBody TestValidatorParam testValidatorParam){
        return CommonResult.success(null);
    }
}
