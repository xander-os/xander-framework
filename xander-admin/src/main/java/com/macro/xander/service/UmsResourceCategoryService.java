package com.macro.xander.service;


import com.macro.xander.model.UmsResourceCategory;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/10 15:38
 * @email ：zhrunxin33@gmail.com
 * @description：后台资源分类管理Service
 */
public interface UmsResourceCategoryService {

    /**
     * 获取所有资源分类
     */
    List<UmsResourceCategory> listAll();

    /**
     * 创建资源分类
     */
    int create(UmsResourceCategory umsResourceCategory);

    /**
     * 修改资源分类
     */
    int update(Long id, UmsResourceCategory umsResourceCategory);

    /**
     * 删除资源分类
     */
    int delete(Long id);
}
