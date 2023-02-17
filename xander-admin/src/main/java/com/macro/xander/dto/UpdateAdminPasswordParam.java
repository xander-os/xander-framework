package com.macro.xander.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/17 17:12
 * @email ：zhrunxin33@gmail.com
 * @description：更新密码请求参数
 */
@Getter
@Setter
public class UpdateAdminPasswordParam {
    @NotEmpty
    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @NotEmpty
    @ApiModelProperty(value = "旧密码",required = true)
    private String oldPassword;


    @NotEmpty
    @ApiModelProperty(value = "新密码",required = true)
    private String newPassword;
}
