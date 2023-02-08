package com.macro.xander.common.api;

import org.springframework.data.domain.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/3 11:53
 * @email ：zhrunxin33@gmail.com
 * @description：统一分页返回对象
 */
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;

    /**
     * 将PageHelper分页后的list转为分页信息
     * 该种方法支持常见的 12 种数据库
     * Oracle,MySql,MariaDB,SQLite,DB2,PostgreSQL,SqlServer等
     * @param list
     * @param <T>
     * @return
     */
    public static <T> CommonPage<T> restPage(List<T> list){
        CommonPage<T> result = new CommonPage<>();
        // 这里list是父类引用指向子类实例，子类是Page类对象
        PageInfo<T> pageInfo = new PageInfo<>(list);
        result.setTotalPage(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

    /**
     * 将SpringData分页后的list转为分页信息
     * 这种方法用在分页助手不支持的MongoDB和ES
     * @param pageInfo
     * @param <T>
     * @return
     */
    public static <T> CommonPage<T> restPage(Page<T> pageInfo){
        CommonPage<T> result = new CommonPage<>();
        result.setTotalPage(pageInfo.getTotalPages());
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }


    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
