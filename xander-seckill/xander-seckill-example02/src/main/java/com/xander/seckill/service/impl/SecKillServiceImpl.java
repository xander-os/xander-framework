package com.xander.seckill.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
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
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.key.orderId}")
    private String REDIS_KEY_ORDER_ID;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.key.seckill.end}")
    private String REDIS_KEY_FLASH_END;
    @Value("${redis.key.seckill.path}")
    private String REDIS_KEY_FLASH_PATH;
    @Value("${redis.key.seckill.pathVerify}")
    private boolean REDIS_KEY_FLASH_PATH_VERIFY;
    @Value("${redis.key.seckill.num}")
    private String REDIS_KEY_FLASH_NUM;
    @Value("${redis.key.seckill.limit}")
    private String REDIS_KEY_FLASH_LIMIT;
    @Value("${redis.expire.seckill}")
    private Long REDIS_FLASH_END_EXPIRE;

    @Override
    public Boolean secKill(String path, Long goodsId, Long promotionId, FlashOrderParam flashOrderParam) {
        // 获取当前登录用户
        UmsMember member = memberService.getCurrentMember();
        // ①校验Path路径
        String usefulPath = (String)redisService.get(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_PATH + ":" + member.getUsername());
        if (REDIS_KEY_FLASH_PATH_VERIFY && !StringUtils.equals(path,usefulPath)){
            Asserts.failed("限时购路径错误！");
        }

        // ②校验该限时购是否合法并预减库存（包括1.是否开启，2.场次是否到点，3.是否结束，4.是否达到限时购上限）
        Boolean result = checkIfLegalAndDecrStock(goodsId, promotionId, flashOrderParam,member);
        // 预减库存失败直接返回
        if(!result){
            return false;
        }


        // ④ 实际减库存相关（库存是否足够，MySQL）。保证原子性操作，需要开启事务
        Boolean bool = createOrder(goodsId,flashOrderParam,member,promotionId);
        // RabbitMQ发送死信队列，延时取消订单（返回库存）
        //sendDelayMessageCancelOrder(order.getId());
        return true;
    }

    public Boolean createOrder(Long goodsId, FlashOrderParam flashOrderParam,UmsMember member,Long promotionId) {
        // 查询该次限时购的信息
        SmsFlashPromotionProductRelation sessionStock = getSessionStock(goodsId, promotionId);
        if (sessionStock == null){
            Asserts.failed("查询不到该场次存在限时购！");
        }
        // 从限时购反查询商品表和库存表
        PmsProduct pmsProduct = pmsProductMapper.selectByPrimaryKey(goodsId);
        PmsSkuStockExample pmsSkuStockExample = new PmsSkuStockExample();
        pmsSkuStockExample.createCriteria().andIdEqualTo(flashOrderParam.getProductSkuId()).andProductIdEqualTo(goodsId);
        List<PmsSkuStock> pmsSkuStocks = pmsSkuStockMapper.selectByExample(pmsSkuStockExample);
        if(pmsProduct == null || CollectionUtil.isEmpty(pmsSkuStocks)){
            Asserts.failed("错误的商品ID或者SkuID！！");
        }
        // 减库存（限时购表和商品库存表）
        // 1.两个库存之间不用保证原子性
        // 2.先减限时购表，再减商品库存表（不会出现安全问题）
        // 3.失败，Redis标志结束后直接返回
        if(pmsSkuStocks.get(0).getStock() < flashOrderParam.getCount()){
            // 限时购限制数大于1时，可能会出现库存为1，但是会员买2个商品的情况
            Asserts.failed("商品库存不足！！");
        }

        // sku库存减少
        int reduceStock = secKillMapper.reduceStock(pmsSkuStocks.get(0).getId(), flashOrderParam.getCount());
        if (reduceStock <= 0){
            Asserts.failed("限时购商品sku库存不足！");
        }

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
        // 下单成功后，给redis打个标识
        redisService.set(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_NUM + ":"
                +member.getUsername() + "_" + promotionId + "_" + goodsId,"1",calcRemainSec(10));
        return null;
    }


    /**
     * 检查订单合法性及预减库存
     */
    private Boolean checkIfLegalAndDecrStock(Long goodsId, Long promotionId, FlashOrderParam flashOrderParam, UmsMember member) {
        // 是否Redis标志已经结束
        Boolean overSign = (Boolean)redisService.get(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_END + ":"
                + promotionId + "_" + goodsId + "_" + flashOrderParam.getProductSkuId());
        if(overSign != null && overSign) {
            Asserts.failed("该限时购已结束！！");
        }
        // TODO 查询是否超过限时购数量
        String limit = (String) redisService.get(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_LIMIT + ":"
                + promotionId + "_" + goodsId);
        String num = (String) redisService.get(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_NUM + ":"
                +member.getUsername() + "_" + promotionId + "_" + goodsId);
        // TODO 校验秒杀次数
//        if(num != null && limit != null && Integer.valueOf(num) + flashOrderParam.getCount() <= Integer.valueOf(limit)){
//
//        }
        if(StringUtils.isNotBlank(num)){
            Asserts.failed("已超过限时购上限！！");
        }

        // 预减库存,成功就创建订单。失败有两种可能：
        // Ⅰ还没把库存缓存进去 (Redis返回null)
        // Ⅱ库存不够减 (返回-1)
        String script = "local key = KEYS[1]\n" +
                "local remainStock = tonumber(redis.call('get',key))\n" +
                "if(remainStock == nil) then\n" +
                "    return nil\n" +
                "end\n" +
                "if remainStock ~= nil and remainStock > 0 then\n" +
                "    local current = tonumber(redis.call('decr', key))\n" +
                "    return current\n" +
                "end\n" +
                "if remainStock ~= nil and remainStock == 0 then\n" +
                "    return -1\n" +
                "end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        // TODO 想想怎么处理一致性出现超卖问题
        // 方案一：针对该商品使用分布式锁（优点：简单   缺点：单个商品的限购数量太高会有性能问题）
        // 方案二：库存信息预先缓存到Redis(优点：不用考虑超卖的情况，为空直接返回失败  缺点：需要额外作缓存处理，定时或者初始化)
        // 这里选用方案二
        synchronized (this) {
            Long decrResult = redisTemplate.execute(redisScript, Arrays.asList(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_NUM + ":"
                    + promotionId + "_" + goodsId + "_" + flashOrderParam.getProductSkuId()), "");
            // 减库存成功直接返回
            if (decrResult != null && decrResult >= 0l){
                return true;
            }
            // 库存不足
            if (decrResult != null && decrResult == -1l){
                // Redis标志该限时购结束
                redisService.set(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_END + ":"
                        + promotionId + "_" + goodsId + "_" + flashOrderParam.getProductSkuId(),true,calcRemainSec(10));
                return false;
            }
            // 查询限时购表是否在时间范围内开启
            SmsFlashPromotionExample smsFlashPromotionExample = new SmsFlashPromotionExample();
            smsFlashPromotionExample.createCriteria().andIdEqualTo(promotionId).andStatusEqualTo(1)
                    .andStartDateLessThanOrEqualTo(DateTime.now())
                    .andEndDateGreaterThanOrEqualTo(DateTime.now());
            List<SmsFlashPromotion> smsFlashPromotions = smsFlashPromotionMapper.selectByExample(smsFlashPromotionExample);
            if (CollectionUtil.isEmpty(smsFlashPromotions)){
                Asserts.failed("该限时购暂未开启！");
            }

            // 查询该场次是否到点
            SmsFlashPromotionProductRelation sessionStock = getSessionStock(goodsId, promotionId);
            if (sessionStock == null) {
                Asserts.failed("查询不到该场次存在限时购！");
            }
            // 时间计算器（距离整点的时间，因为秒杀活动都是整点开始,并且会隔一个小时，因此可以巧妙地设置过期时间）
            redisService.set(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_NUM + ":"
                    + promotionId + "_" + goodsId + "_" + flashOrderParam.getProductSkuId(), sessionStock.getFlashPromotionCount(), calcRemainSec(10));
            // 缓存了库存后，再预减库存

            decrResult = redisTemplate.execute(redisScript, Arrays.asList(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_NUM + ":"
                    + promotionId + "_" + goodsId + "_" + flashOrderParam.getProductSkuId()), "");
            if(decrResult >=0 ){
                return true;
            }
        }

        return false;
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

    @Override
    public String generatePath(Long goodsId, Long promotionId) {
        UmsMember member = memberService.getCurrentMember();
        String username = member.getUsername();
        // 确保每个商品每个场次的路径都不一样(有效期60秒)
        String usefulPath = DigestUtils.md5Hex(username + DateTime.now());
        redisService.set(REDIS_DATABASE + ":" + REDIS_KEY_FLASH_PATH + ":" + username,usefulPath,60l);
        return usefulPath;
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

    /**
     * 计算离下一个小时剩余的秒数（确保时间>0）
     * offset 偏移数
     */
    private Long calcRemainSec(int offset){
        DateTime dateTime = DateUtil.endOfHour(new DateTime());
        long between = DateUtil.between(dateTime, new DateTime(), DateUnit.SECOND, true);
        return between + offset;
    }
}
