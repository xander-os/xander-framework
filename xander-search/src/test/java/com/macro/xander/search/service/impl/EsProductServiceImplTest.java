package com.macro.xander.search.service.impl;

import com.macro.xander.search.dao.EsProductDao;
import com.macro.xander.search.domain.EsProduct;
import com.macro.xander.search.service.EsProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.List;


@SpringBootTest
class EsProductServiceImplTest {

    @Autowired
    private EsProductService esProductService;

    @Autowired
    private EsProductDao esProductDao;

    @Test
    public void importAll(){
        List<EsProduct> allEsProductList = esProductDao.getAllEsProductList(null);
        int i = allEsProductList.size();
        Assert.isTrue(i > 0);
    }

    @Test
    public void queryAll(){
        String keyword = "8天超长待机";
        Page<EsProduct> search = esProductService.search(keyword, 0, 10);
        System.out.println(search.toList());
    }

}