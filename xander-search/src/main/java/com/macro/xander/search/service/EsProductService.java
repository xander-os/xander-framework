package com.macro.xander.search.service;

import com.macro.xander.search.domain.EsProduct;
import com.macro.xander.search.domain.EsProductRelatedInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/16 17:02
 * @email ：zhrunxin33@gmail.com
 * @description：商品搜索管理Service
 */
public interface EsProductService {
    /**
     * 从数据库中导入所有商品到ES
     */
    int importAll();

    /**
     * 根据ID删除一条数据
     */
    void delete(Long id);

    /**
     * 根据ID创建数据（先去数据库查询）
     */
    EsProduct create(Long id);

    /**
     * 批量删除数据
     */
    void delete(List<Long> ids);

    /**
     * 根据关键字查询名称或副标题
     * @param keyword 关键字
     * @param pageNum 当前页码
     * @param pageSize 页大小
     * @return
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 根据关键字搜索名称或者副标题复合查询
     */
    Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 根据商品id推荐相关商品
     */
    Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize);

    /**
     * 获取搜索词相关品牌、分类、属性
     */
    EsProductRelatedInfo searchRelatedInfo(String keyword);
}
