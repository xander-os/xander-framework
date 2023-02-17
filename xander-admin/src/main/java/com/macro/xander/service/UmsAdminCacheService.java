package com.macro.xander.service;

import com.macro.xander.model.UmsAdmin;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/17 17:33
 * @email ：zhrunxin33@gmail.com
 * @description：
 */
public interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);

    /**
     * 获取后台缓存数据
     */
    UmsAdmin getAdmin(Long adminId);

    /**
     * 设置后台用户缓存
     * @param umsAdmin
     */
    void setAdmin(UmsAdmin umsAdmin);
}
