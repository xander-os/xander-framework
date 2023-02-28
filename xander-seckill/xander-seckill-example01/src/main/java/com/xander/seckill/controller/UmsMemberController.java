package com.xander.seckill.controller;

import com.macro.xander.model.UmsMember;
import com.xander.seckill.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/28 16:44
 * @email ：zhrunxin33@gmail.com
 * @description：提供会员查询接口
 */
@RestController
@Api(value = "会员查询接口",tags = "UmsMemberController")
@RequestMapping("/member")
public class UmsMemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/loadUserByUsername")
    @ApiOperation(value="根据用户名查询用户")
    public UmsMember loadUserByUsername(@RequestParam("username") @NotEmpty String username){
        return memberService.loadUserByUsername(username);
    }
}
