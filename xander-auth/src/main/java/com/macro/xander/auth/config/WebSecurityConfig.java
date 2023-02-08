package com.macro.xander.auth.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/6 20:37
 * @email ：zhrunxin33@gmail.com
 * @description：SpringSecurity配置
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 配置黑白名单Url路径
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .antMatchers("/rsa/publicKey").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/oauth/token").permitAll()

                .antMatchers("/doc.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger/**").permitAll()
                .antMatchers("/*/*.js").permitAll()
                .antMatchers("/*/*.css").permitAll()
                .antMatchers("/*/*.png").permitAll()
                .antMatchers("/*/*.ico").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest().authenticated();

        //解决跨域
        http.csrf().disable();
    }

    /**
     * AuthenticationManager用于处理Authentication请求，
     * Spring Security在进行身份验证时，会创建身份验证令牌，即Authentication实例，
     * 提供给AuthenticationManager接口的实现类进行处理，由实现类中的AuthenticationProvider列表进行验证。
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 使用BCryptPasswordEncoder加密方法，采用了SHA-256+随机盐+密钥对密码进行加密。
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
