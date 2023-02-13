package com.macro.xander.service;

import com.macro.xander.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/13 9:40
 * @email ：zhrunxin33@gmail.com
 * @description：认证服务远程调用
 */
@FeignClient("xander-auth")
public interface AuthService {

    /**
     * 请求认证中心获取Token接口
     * @param parameters
     * @return
     */
    @PostMapping(value = "/oauth/token")
    CommonResult getAccessToken(@RequestParam Map<String, String> parameters);
}
