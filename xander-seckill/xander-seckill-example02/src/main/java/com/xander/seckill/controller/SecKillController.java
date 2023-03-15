package com.xander.seckill.controller;

import com.macro.xander.common.api.CommonResult;
import com.xander.seckill.dto.FlashOrderParam;
import com.xander.seckill.service.SecKillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/24 9:48
 * @email ：zhrunxin33@gmail.com
 * @description：秒杀接口
 */
@RestController
@Api(value = "秒杀接口",tags = "SecKillController")
@RequestMapping("/order")
public class SecKillController {

    @Autowired
    private SecKillService secKillService;

    @GetMapping("/path/{goodsId}/{promotionId}")
    @ApiOperation(value="获取秒杀路径")
    public CommonResult<Object> path(@PathVariable("goodsId") Long goodsId, @PathVariable("promotionId") Long promotionId){
        String path = secKillService.generatePath(goodsId, promotionId);
        return CommonResult.success(path);
    }

    /**
     * QPS:30
     */
    @PostMapping("/{path}/secKill")
    @ApiOperation(value="秒杀")
    public CommonResult<Object> secKill(@PathVariable("path") String path, @RequestBody FlashOrderParam param, @RequestParam("goodsId") Long goodsId, @RequestParam("promotionId") Long promotionId){
        Boolean result = secKillService.secKill(path,goodsId, promotionId,param);
        return CommonResult.success("SecKill Success!");
    }

    /**
     * QPS:1000
     */
    @PostMapping("/qps")
    @ApiOperation(value="qps测试")
    public CommonResult<Object> qps(){
        return CommonResult.success("SecKill Success!");
    }
}
