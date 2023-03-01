package com.xander.seckill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/27 12:22
 * @email ：zhrunxin33@gmail.com
 * @description：秒杀订单参数
 */
@Data
public class FlashOrderParam {
    @ApiModelProperty("收货地址ID")
    private Long memberReceiveAddressId;
    @ApiModelProperty("支付方式")
    private Integer payType;
    @NotEmpty
    @Min(value = 1,message = "秒杀数量不得小于1")
    @ApiModelProperty("秒杀数量")
    private Integer count;
    @NotEmpty
    @ApiModelProperty("秒杀商品Sku")
    private Long productSkuId;
}
