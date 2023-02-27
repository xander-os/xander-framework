package com.xander.seckill.dao;

import com.macro.xander.model.SmsFlashPromotionProductRelation;
import org.apache.ibatis.annotations.Param;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/25 19:14
 * @email ：zhrunxin33@gmail.com
 * @description：秒杀数据库操作
 */
public interface SecKillMapper {

    /**
     * 根据现在系统时间获取限时购场次库存
     */
    SmsFlashPromotionProductRelation getSessionStock(@Param("goodsId")Long goodsId, @Param("promotionId")Long promotionId);

    /**
     * 减限时购库存
     */
    int reduceFlashStock(@Param("flashSessionRelationId") Long flashSessionRelationId,@Param("quantity") Integer quantity);

    /**
     * 减sku的库存
     */
    int reduceStock(@Param("skuId") Long skuId,@Param("quantity") Integer quantity);
}
