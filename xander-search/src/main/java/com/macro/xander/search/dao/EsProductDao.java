package com.macro.xander.search.dao;

import com.macro.xander.search.domain.EsProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/16 14:28
 * @email ：zhrunxin33@gmail.com
 * @description：查询商品表，同步到Es（此处只能做查询操作）
 */

public interface EsProductDao {

    /**
     * 获取指定ID或者全部的搜索商品
     */
    List<EsProduct> getAllEsProductList(@Param("id") Long id);
}
