package com.macro.xander.dao;

import com.macro.xander.model.UmsMenu;
import com.macro.xander.model.UmsResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/17 17:12
 * @email ：zhrunxin33@gmail.com
 * @description：自定义后台角色管理Dao
 */
public interface UmsRoleDao {
    /**
     * 根据后台用户ID获取菜单
     */
    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);
    /**
     * 根据角色ID获取菜单
     */
    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);
    /**
     * 根据角色ID获取资源
     */
    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);
}
