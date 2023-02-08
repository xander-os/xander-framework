package com.macro.xander.auth.controller;

import com.macro.xander.auth.domain.Oauth2TokenDto;
import com.macro.xander.common.api.CommonResult;
import com.macro.xander.common.constant.AuthConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 11:33
 * @email ：zhrunxin33@gmail.com
 * @description：自定义Oauth2获取令牌接口
 */
@RestController
@RequestMapping("/oauth")
@Api(tags = "AuthController", value = "认证中心登录认证")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    /**
     * 重定义获取Token令牌接口覆盖默认的TokenEndpoint.postAccessToken()
     */
    @ApiOperation("Oauth2获取token")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public CommonResult<Oauth2TokenDto> postAccessToken(HttpServletRequest request,
                                                        @ApiParam("授权模式") @RequestParam String grant_type,
                                                        @ApiParam("Oauth2客户端ID") @RequestParam String client_id,
                                                        @ApiParam("Oauth2客户端秘钥") @RequestParam String client_secret,
                                                        @ApiParam("刷新token") @RequestParam(required = false) String refresh_token,
                                                        @ApiParam("登录用户名") @RequestParam(required = false) String username,
                                                        @ApiParam("登录密码") @RequestParam(required = false) String password) throws HttpRequestMethodNotSupportedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type",grant_type);
        parameters.put("client_id",client_id);
        parameters.put("client_secret",client_secret);
        parameters.putIfAbsent("refresh_token",refresh_token);
        parameters.putIfAbsent("username",username);
        parameters.putIfAbsent("password",password);
        // 封装OAuth2AccessToken，调用Oauth2默认的登陆接口
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(request.getUserPrincipal(), parameters).getBody();
        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead(AuthConstant.JWT_TOKEN_PREFIX).build();

        return CommonResult.success(oauth2TokenDto);
    }

    @ApiOperation("Oauth2获取token11")
    @RequestMapping(value = "/token2", method = {RequestMethod.GET,RequestMethod.POST})
    public CommonResult testDd(){
        return CommonResult.success("66");
    }
}
