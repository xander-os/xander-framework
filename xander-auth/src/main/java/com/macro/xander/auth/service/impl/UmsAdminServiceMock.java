package com.macro.xander.auth.service.impl;

import com.macro.xander.auth.service.UmsAdminService;
import com.macro.xander.common.domain.UserDto;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 10:12
 * @email ：zhrunxin33@gmail.com
 * @description：模拟调用Admin微服务
 */
@Service
public class UmsAdminServiceMock implements UmsAdminService {

    private static UserDto mock = null;

    @PostConstruct
    public void init(){
        UserDto userDto = new UserDto();
        userDto.setClientId("admin-app");
        userDto.setId(1l);
        userDto.setStatus(1);
//        userDto.setRoles();
        userDto.setUsername("admin");
        userDto.setPassword("$2a$10$.E1FokumK5GIXWgKlg.Hc.i/0/2.qdAwYFL1zc5QHdyzpXOr38RZO");
        mock = userDto;
    }

    @Override
    public UserDto loadUserByUsername(String userName) {
        return mock;
    }
}
