package com.macro.xander.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/4 11:10
 * @email ：zhrunxin33@gmail.com
 * @description：用户登陆信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Integer status;
    private String clientId;
    private List<String> roles;

}