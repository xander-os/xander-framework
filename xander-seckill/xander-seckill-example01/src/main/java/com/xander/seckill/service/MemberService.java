package com.xander.seckill.service;

import com.macro.xander.model.UmsMember;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/27 15:20
 * @email ：zhrunxin33@gmail.com
 * @description：客户相关
 */
public interface MemberService {
    /**
     * 获取当前登录用户
     */
    UmsMember getCurrentMember();

    /**
     * 根据客户ID查询信息
     */
    UmsMember getById(Long memberId);
}
