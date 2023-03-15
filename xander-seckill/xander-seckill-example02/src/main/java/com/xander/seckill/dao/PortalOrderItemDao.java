package com.xander.seckill.dao;

import com.macro.xander.model.OmsOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/25 20:49
 * @email ：zhrunxin33@gmail.com
 * @description：
 */
public interface PortalOrderItemDao {
    int insertList(@Param("list") List<OmsOrderItem> list);
}
