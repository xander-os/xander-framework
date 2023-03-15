package com.xander.seckill.service;

import com.macro.xander.model.SmsFlashPromotionProductRelation;
import com.macro.xander.model.UmsMember;
import com.macro.xander.model.UmsMemberReceiveAddress;
import com.xander.seckill.dto.FlashOrderParam;
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
     * @param path
     * @param goodsId 商品ID
     * @param promotionId 限时购ID
     */
    Boolean secKill(String path, Long goodsId, Long promotionId, FlashOrderParam param);

    /**
     * 获取当前场次库存
     */
    SmsFlashPromotionProductRelation getSessionStock(Long goodsId, Long promotionId);

    /**
     * 获取用户收货地址
     */
    UmsMemberReceiveAddress getItem(Long id);

    /**
     * 生成秒杀路径
     */
    String generatePath(Long goodsId, Long promotionId);

    /**
     * 创建订单(需要开启事务)
     */
    @Transactional
    public Boolean createOrder(Long goodsId, FlashOrderParam flashOrderParam, UmsMember member, Long promotionId);
}
