package com.macro.xander.auth.service.impl;

import com.macro.xander.auth.service.UmsMemberService;
import com.macro.xander.common.domain.UserDto;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 10:13
 * @email ：zhrunxin33@gmail.com
 * @description：模拟调用用户微服务
 */
//@Service
public class UmsMemberServiceMock implements UmsMemberService {

    private static UserDto mock = null;

    @PostConstruct
    public void init(){
        UserDto userDto = new UserDto();
        userDto.setClientId("portal-app");
        userDto.setId(1l);
        userDto.setStatus(1);
//        userDto.setRoles();
        userDto.setUsername("test");
        userDto.setPassword("$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG");
        mock = userDto;
    }

    @Override
    public UserDto loadUserByUsername(String userName) {
        return mock;
    }
}
