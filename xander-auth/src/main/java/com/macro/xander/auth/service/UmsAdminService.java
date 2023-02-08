package com.macro.xander.auth.service;

import com.macro.xander.common.domain.UserDto;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 10:08
 * @email ：zhrunxin33@gmail.com
 * @description：管理员接口
 */
public interface UmsAdminService {

    /**
     * 根据管理员名获取管理员信息
     */
    UserDto loadUserByUsername(String userName);
}
