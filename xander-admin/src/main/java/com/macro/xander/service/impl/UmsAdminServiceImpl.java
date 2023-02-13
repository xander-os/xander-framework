package com.macro.xander.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.macro.xander.common.api.CommonResult;
import com.macro.xander.common.api.ResultCode;
import com.macro.xander.common.constant.AuthConstant;
import com.macro.xander.common.exception.Asserts;
import com.macro.xander.dto.UmsAdminParam;
import com.macro.xander.mapper.UmsAdminMapper;
import com.macro.xander.model.UmsAdmin;
import com.macro.xander.model.UmsAdminExample;
import com.macro.xander.service.AuthService;
import com.macro.xander.service.UmsAdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/12 12:14
 * @email ：zhrunxin33@gmail.com
 * @description：后台用户管理
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    @Autowired
    private UmsAdminMapper umsAdminMapper;
    @Autowired
    private AuthService authService;

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = umsAdminMapper.selectByExample(example);
        if (umsAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = BCrypt.hashpw(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        umsAdminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public CommonResult login(String username, String password) {
        if(StrUtil.isEmpty(username) || StrUtil.isEmpty(password)){
            Asserts.failed("用户名或者密码不能为空");
        }
        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstant.ADMIN_CLIENT_ID);
        params.put("client_secret","123456");
        params.put("grant_type","password");
        params.put("username",username);
        params.put("password",password);
        CommonResult authResult = authService.getAccessToken(params);
        if (ResultCode.SUCCESS.getCode() == authResult.getCode() && authResult.getData() != null){
            // 添加登录日志
//            insertLoginLog(username);
        }
        return authResult;
    }
}
