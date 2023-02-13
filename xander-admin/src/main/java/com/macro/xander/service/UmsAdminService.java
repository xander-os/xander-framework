package com.macro.xander.service;

import com.macro.xander.common.api.CommonResult;
import com.macro.xander.dto.UmsAdminParam;
import com.macro.xander.model.UmsAdmin;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/12 12:11
 * @email ：zhrunxin33@gmail.com
 * @description：后台用户管理
 */
public interface UmsAdminService {
    /**
     * 后台用户注册
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 后台用户登陆
     * @param username 用户名
     * @param password 密码
     * @return 认证中心返回结果
     */
    CommonResult login(String username, String password);
}
