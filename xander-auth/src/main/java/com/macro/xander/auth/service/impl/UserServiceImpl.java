package com.macro.xander.auth.service.impl;

import com.macro.xander.auth.constant.MessageConstant;
import com.macro.xander.auth.domain.SecurityUser;
import com.macro.xander.auth.service.UmsAdminService;
import com.macro.xander.auth.service.UmsMemberService;
import com.macro.xander.common.constant.AuthConstant;
import com.macro.xander.common.domain.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/6 22:10
 * @email ：zhrunxin33@gmail.com
 * @description：用户管理业务类
 * (使用Security需要实现UserDetails实体接口，UserDetailsService接口)
 */
@Service
public class UserServiceImpl implements UserDetailsService{

        @Autowired
        private UmsAdminService adminService;
        @Autowired
        private UmsMemberService memberService;
        @Autowired
        private HttpServletRequest request;

        /**
         * 查看用户状态是否正常，并把用户信息转换为SecurityUser
         */
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            String clientId = request.getParameter("client_id");
            UserDto userDto;
            if(AuthConstant.ADMIN_CLIENT_ID.equals(clientId)){
                userDto = adminService.loadUserByUsername(username);
            }else{
                userDto = memberService.loadUserByUsername(username);
            }
            if (userDto==null) {
                throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
            }
            userDto.setClientId(clientId);
            SecurityUser securityUser = new SecurityUser(userDto);
            if (!securityUser.isEnabled()) {
                throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
            } else if (!securityUser.isAccountNonLocked()) {
                throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
            } else if (!securityUser.isAccountNonExpired()) {
                throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
            } else if (!securityUser.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
            }
            return securityUser;
        }

    }
