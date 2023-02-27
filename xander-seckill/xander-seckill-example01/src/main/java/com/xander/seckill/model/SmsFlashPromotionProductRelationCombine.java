package com.xander.seckill.model;

import com.macro.xander.model.SmsFlashPromotionProductRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/25 19:05
 * @email ：zhrunxin33@gmail.com
 * @description：场次和该场次库存关系
 */
@Data
public class SmsFlashPromotionProductRelationCombine extends SmsFlashPromotionProductRelation {

    @ApiModelProperty(value = "场次名称")
    private String name;

    @ApiModelProperty(value = "每日开始时间")
    private Date startTime;

    @ApiModelProperty(value = "每日结束时间")
    private Date endTime;

    @ApiModelProperty(value = "启用状态：0->不启用；1->启用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
