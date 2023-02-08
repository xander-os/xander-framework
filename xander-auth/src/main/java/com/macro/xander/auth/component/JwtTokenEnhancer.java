package com.macro.xander.auth.component;

import com.macro.xander.auth.domain.SecurityUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/6 22:04
 * @email ：zhrunxin33@gmail.com
 * @description：JWT增强器(在负载中增加信息)
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {

    /**
     * 增强处理UserServiceImpl.loadUserByUsername()的登陆信息
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Map<String, Object> info = new HashMap<>();
        //把用户ID设置到JWT中
        info.put("id", securityUser.getId());
        info.put("client_id",securityUser.getClientId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}
