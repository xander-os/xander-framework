package com.macro.xander.auth.service;

import com.macro.xander.common.domain.UserDto;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 10:10
 * @email ：zhrunxin33@gmail.com
 * @description：用户接口
 */
public interface UmsMemberService {

    /**
     * 根据用户名获取用户信息
     */
    UserDto loadUserByUsername(String userName);
}
