package com.xander.seckill.service;

import com.macro.xander.model.SmsFlashPromotionProductRelation;
import com.xander.seckill.dto.FlashOrderParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/24 10:03
 * @email ：zhrunxin33@gmail.com
 * @description：秒杀逻辑处理
 */
public interface SecKillService {

    /**
     * 秒杀接口
     * @param goodsId 商品ID
     * @param promotionId 限时购ID
     */
    @Transactional
    Boolean secKill(Long goodsId, Long promotionId, FlashOrderParam param);

    /**
     * 获取当前场次库存
     */
    SmsFlashPromotionProductRelation getSessionStock(Long goodsId, Long promotionId);

}
