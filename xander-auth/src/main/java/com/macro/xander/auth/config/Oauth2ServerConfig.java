package com.macro.xander.auth.config;

import com.macro.xander.auth.component.JwtTokenEnhancer;
import com.macro.xander.auth.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 10:34
 * @email ：zhrunxin33@gmail.com
 * @description：认证服务器配置
 */
@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 以下变量用@AllArgsConstructor实现构造器注入
     * Spring 框架自身的一个特性，对于一个 SpringBean 来说，如果其只有一个构造方法，那么 Spring 会使用该构造方法并自动注入其所需要的全部依赖
     */
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenEnhancer jwtTokenEnhancer;

    /**
     *  允许表单认证,针对/oauth/token端点
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
    }

    /**
     * 配置获取ClientDetails信息方式,
     * 能够使用内存或 JDBC 方式实现获取已注册的客户端详情
     * */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置内存中校验用户信息
        clients.inMemory()
                .withClient("admin-app")
                .secret(passwordEncoder.encode("123456"))
                .scopes("all")
                // 支持密码和刷新Token的鉴权类型
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600*24)
                .refreshTokenValiditySeconds(3600*24*7)
                .and()
                .withClient("portal-app")
                .secret(passwordEncoder.encode("123456"))
                .scopes("all")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600*24)
                .refreshTokenValiditySeconds(3600*24*7);
    }

    /**
     * 装载Endpoints所有相关的类配置
     * （AuthorizationServer、TokenServices、TokenStore、ClientDetailsService、UserDetailsService）
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 封装JWT内容增强器链
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());
        enhancerChain.setTokenEnhancers(delegates);

        endpoints.authenticationManager(authenticationManager) //配置认证管理器（如果GrantTypes有password方式，必须要配置）
                .userDetailsService(userDetailsService) //配置加载用户信息的服务
                .accessTokenConverter(accessTokenConverter()) //配置Token加解密转换器
                .tokenEnhancer(enhancerChain); //配置JWT的内容增强器
    }

    /**
     * 使用非对称加密算法加密Token进行签名(生成Token加解密转换器)
     */
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    /**
     * 使用非对称加密算法,生成密钥串
     * 同时需要开放接口提供公钥给客户端
     */
    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
    }
}
