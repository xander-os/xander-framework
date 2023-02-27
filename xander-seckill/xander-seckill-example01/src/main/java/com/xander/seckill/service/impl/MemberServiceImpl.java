package com.xander.seckill.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.macro.xander.common.api.ResultCode;
import com.macro.xander.common.constant.AuthConstant;
import com.macro.xander.common.domain.UserDto;
import com.macro.xander.common.exception.Asserts;
import com.macro.xander.mapper.UmsMemberMapper;
import com.macro.xander.model.UmsMember;
import com.macro.xander.model.UmsMemberExample;
import com.xander.seckill.service.MemberCacheService;
import com.xander.seckill.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/27 15:22
 * @email ：zhrunxin33@gmail.com
 * @description：客户相关
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MemberCacheService memberCacheService;
    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Override
    public UmsMember getCurrentMember() {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if(StrUtil.isEmpty(userStr)){
            Asserts.failed(ResultCode.UNAUTHORIZED);
        }
        UserDto userDto = JSONUtil.toBean(userStr, UserDto.class);
        UmsMember member = memberCacheService.getMember(userDto.getId());
        if(member!=null){
            return member;
        }else{
            member = getById(userDto.getId());
            memberCacheService.setMember(member);
            return member;
        }
    }

    public UmsMember getById(Long id) {
        return umsMemberMapper.selectByPrimaryKey(id);
    }
}
