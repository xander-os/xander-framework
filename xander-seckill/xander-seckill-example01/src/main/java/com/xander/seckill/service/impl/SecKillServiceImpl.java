package com.xander.seckill.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.macro.xander.common.exception.Asserts;
import com.macro.xander.common.service.RedisService;
import com.macro.xander.mapper.*;
import com.macro.xander.model.*;
import com.xander.seckill.dao.PortalOrderItemDao;
import com.xander.seckill.dao.SecKillMapper;
import com.xander.seckill.dto.FlashOrderParam;
import com.xander.seckill.service.MemberService;
import com.xander.seckill.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/24 10:04
 * @email ：zhrunxin33@gmail.com
 * @description：秒杀逻辑实现
 */
@Service
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private SmsFlashPromotionMapper smsFlashPromotionMapper;
    @Autowired
    private SecKillMapper secKillMapper;
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private PortalOrderItemDao orderItemDao;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PmsProductMapper pmsProductMapper;
    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;
    @Autowired
    private UmsMemberReceiveAddressMapper addressMapper;
    @Value("${redis.key.orderId}")
    private String REDIS_KEY_ORDER_ID;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.key.seckill}")
    private String REDIS_KEY_FLASH_END;
    @Value("${redis.expire.seckill}")
    private Long REDIS_FLASH_END_EXPIRE;

    @Override
    public Boolean secKill(Long goodsId, Long promotionId, FlashOrderParam flashOrderParam) {
        // ①查询限时购表是否在时间范围内开启
        SmsFlashPromotionExample smsFlashPromotionExample = new SmsFlashPromotionExample();
        smsFlashPromotionExample.createCriteria().andIdEqualTo(promotionId).andStatusEqualTo(1)
                .andStartDateLessThanOrEqualTo(DateTime.now())
                .andEndDateGreaterThanOrEqualTo(DateTime.now());
        List<SmsFlashPromotion> smsFlashPromotions = smsFlashPromotionMapper.selectByExample(smsFlashPromotionExample);
        if (CollectionUtil.isEmpty(smsFlashPromotions)){
            Asserts.failed("该限时购暂未开启！");
        }
        // ②查询该场次库存是否足够
        SmsFlashPromotionProductRelation sessionStock = getSessionStock(goodsId, promotionId);
        if (sessionStock == null){
            Asserts.failed("查询不到该场次存在限时购！");
        }
        // ③是否Redis标志已经结束
        String sign = (String)redisService.get(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_END + ":"
                + promotionId + "_" + sessionStock.getFlashPromotionSessionId() + "_" + flashOrderParam.getProductSkuId());
        if(StrUtil.isNotBlank(sign)) {
            Asserts.failed("该限时购已结束！！");
        }
        // ④从限时购反查询商品表和库存表
        PmsProduct pmsProduct = pmsProductMapper.selectByPrimaryKey(goodsId);
        PmsSkuStockExample pmsSkuStockExample = new PmsSkuStockExample();
        pmsSkuStockExample.createCriteria().andIdEqualTo(flashOrderParam.getProductSkuId()).andProductIdEqualTo(goodsId);
        List<PmsSkuStock> pmsSkuStocks = pmsSkuStockMapper.selectByExample(pmsSkuStockExample);
        if(pmsProduct == null || CollectionUtil.isEmpty(pmsSkuStocks)){
            Asserts.failed("错误的商品ID或者SkuID！！");
        }
        if(sessionStock.getFlashPromotionCount() <= 0 || pmsSkuStocks.get(0).getStock() <= 0){
            // 给Redis加上该限时购场次结束标志 Key xander:seckill:end:promotionId_sessionId_skuId
            redisService.set(REDIS_DATABASE+":"+REDIS_KEY_FLASH_END+":"
                            +promotionId+"_"+sessionStock.getFlashPromotionSessionId()+"_"+flashOrderParam.getProductSkuId()
                    ,"TRUE",REDIS_FLASH_END_EXPIRE);
            Asserts.failed("没库存啦，该限时购已秒杀完！！");
        }
        if(pmsSkuStocks.get(0).getStock() < flashOrderParam.getCount()){
            // 限时购限制数大于1时，可能会出现库存为1，但是会员买2个商品的情况
            Asserts.failed("商品库存不足！！");
        }
        if(sessionStock.getFlashPromotionLimit() < flashOrderParam.getCount()){
            Asserts.failed("超过该限时购上限:"+sessionStock.getFlashPromotionLimit()+"件！！");
        }
        // ⑤减库存，下单
        // 限时购库存减少
        int reduceFlashStock = secKillMapper.reduceFlashStock(sessionStock.getId(), flashOrderParam.getCount());
        if (reduceFlashStock <= 0){
            Asserts.failed("限时购当前场次库存不足！");
        }
        // sku库存减少
        int reduceStock = secKillMapper.reduceStock(pmsSkuStocks.get(0).getId(), flashOrderParam.getCount());
        if (reduceStock <= 0){
            Asserts.failed("限时购商品sku库存不足！");
        }
        // TODO 商品库存减少
//        PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(flashOrderParam.getProductSkuId());
//        // 限时购锁定下单商品的库存
//        skuStock.setLockStock(skuStock.getLockStock() + flashOrderParam.getCount());
//        skuStockMapper.updateByPrimaryKeySelective(skuStock);
        //插入order表和order_item表
        //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额（限时购下优惠不可用）
        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(new BigDecimal(0));
        // 金额乘以购买数量，算出总金额
        order.setTotalAmount(sessionStock.getFlashPromotionPrice().multiply(new BigDecimal(flashOrderParam.getCount())));
        order.setFreightAmount(new BigDecimal(0));
        order.setCouponAmount(new BigDecimal(0));
        order.setIntegration(0);
        order.setIntegrationAmount(new BigDecimal(0));
        // 限时购不允许其他优惠
        order.setPayAmount(sessionStock.getFlashPromotionPrice().multiply(new BigDecimal(flashOrderParam.getCount())));
        //转化为订单信息并插入数据库
        UmsMember member = memberService.getCurrentMember();
        order.setMemberId(member.getId());
        order.setCreateTime(new Date());
        order.setMemberUsername(member.getUsername());
        //支付方式：0->未支付；1->支付宝；2->微信
        order.setPayType(0);
        //订单来源：0->PC订单；1->app订单
        order.setSourceType(1);
        //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setStatus(0);
        //订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(0);
        //收货人信息：姓名、电话、邮编、地址
        UmsMemberReceiveAddress address = this.getItem(flashOrderParam.getMemberReceiveAddressId());
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        //0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        //计算赠送积分
        order.setIntegration(0);
        //计算赠送成长值
        order.setGrowth(0);
        //生成订单号
        order.setOrderSn(generateOrderSn(order));
        orderMapper.insert(order);
        // 查询商品
        // 生成Item
        ArrayList<OmsOrderItem> orderItemList = new ArrayList<>();
        //生成下单商品信息
        OmsOrderItem orderItem = new OmsOrderItem();
        orderItem.setProductId(pmsProduct.getId());
        orderItem.setProductName(pmsProduct.getName());
        orderItem.setProductPic(pmsProduct.getPic());
        orderItem.setProductBrand(pmsProduct.getBrandName());
        orderItem.setProductSn(pmsProduct.getProductSn());
        orderItem.setProductPrice(pmsProduct.getPrice());
        orderItemList.add(orderItem);

        for (OmsOrderItem item : orderItemList) {
            item.setOrderId(order.getId());
            item.setOrderSn(order.getOrderSn());
        }
        orderItemDao.insertList(orderItemList);
        // RabbitMQ发送死信队列，延时取消订单（返回库存）
        //sendDelayMessageCancelOrder(order.getId());
        return true;
    }

    @Override
    public SmsFlashPromotionProductRelation getSessionStock(Long goodsId, Long promotionId) {
        return secKillMapper.getSessionStock(goodsId, promotionId);
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        List<UmsMemberReceiveAddress> addressList = addressMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(addressList)){
            return addressList.get(0);
        }
        return null;
    }

    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = REDIS_DATABASE+":"+ REDIS_KEY_ORDER_ID + date;
        Long increment = redisService.incr(key, 1);
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }
}
