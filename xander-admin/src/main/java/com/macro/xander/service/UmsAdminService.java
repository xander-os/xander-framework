package com.macro.xander.service;

import com.macro.xander.common.api.CommonResult;
import com.macro.xander.common.domain.UserDto;
import com.macro.xander.dto.UmsAdminParam;
import com.macro.xander.dto.UpdateAdminPasswordParam;
import com.macro.xander.model.UmsAdmin;
import com.macro.xander.model.UmsResource;
import com.macro.xander.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 根据用户名获取用户管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 根据ID获取管理员
     */
    UmsAdmin getAdminById(Long id);

    /**
     * 根据关键词分页查询用户名或昵称
     */
    List<UmsAdmin> list(String keyword, Integer PageSize, Integer pageNum);

    /**
     * 根据用户ID修改信息
     */
    int update(Long id, UmsAdmin admin);

    /**
     * 删除指定用户
     */
    int delete(Long id);

    /**
     * 修改用户角色信息
     * （涉及先删除原本关系再新增，因此需要加事务）
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roles);

    /**
     * 获取该管理员拥有的可访问权限
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * 修改密码
     */
    int updatePassword(UpdateAdminPasswordParam updateAdminPasswordParma);

    /**
     * 获取用户信息
     */
    UserDto loadUserByUsername(String username);

    /**
     * 获取当前登录用户
     */
    UmsAdmin getCurrentAdmin();

    /**
     * 获取缓存服务
     */
    UmsAdminCacheService getCacheService();

    /**
     * 根据用户ID获取角色列表
     */
    List<UmsRole> getRoleList(Long adminId);
}
