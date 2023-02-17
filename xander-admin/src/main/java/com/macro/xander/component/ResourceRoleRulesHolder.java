package com.macro.xander.component;

import com.macro.xander.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/9 20:20
 * @email ：zhrunxin33@gmail.com
 * @description：角色访问及系统资源分配关系
 */
@Component
public class ResourceRoleRulesHolder {

    @Autowired
    private UmsResourceService resourceService;

    @PostConstruct
    public void initResourceRolesMap(){
        resourceService.initResourceRolesMap();
    }
}
