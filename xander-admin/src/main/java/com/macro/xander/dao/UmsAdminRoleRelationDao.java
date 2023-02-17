package com.macro.xander.dao;

import com.macro.xander.model.UmsAdminRoleRelation;
import com.macro.xander.model.UmsResource;
import com.macro.xander.model.UmsRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/17 18:13
 * @email ：zhrunxin33@gmail.com
 * @description：自定义用户和角色关系
 */
public interface UmsAdminRoleRelationDao {

    /**
     * 批量增加用户和角色关系
     */
    int insertList(@Param("list")List<UmsAdminRoleRelation> umsAdminRoleRelationList);

    /**
     * 获取用于所有角色
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

    /**
     * 获取用户所有可访问资源
     */
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);
}
