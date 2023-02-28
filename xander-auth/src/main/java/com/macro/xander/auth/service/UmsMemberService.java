package com.macro.xander.auth.service;

import com.macro.xander.common.domain.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 10:10
 * @email ：zhrunxin33@gmail.com
 * @description：用户接口
 */
@FeignClient("xander-seckill")
public interface UmsMemberService {

    /**
     * 根据用户名获取用户信息
     */
    @RequestMapping(value = "/member/loadUserByUsername",method = RequestMethod.GET)
    UserDto loadUserByUsername(@RequestParam("username") String username);
}
