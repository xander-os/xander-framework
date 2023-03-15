package com.xander.seckill.service;

import com.macro.xander.model.UmsMember;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/27 15:24
 * @email ：zhrunxin33@gmail.com
 * @description：客户缓存相关
 */
public interface MemberCacheService {

    /**
     * 删除会员用户缓存
     */
    void delMember(Long memberId);

    /**
     * 设置验证码
     */
    void setAuthCode(String telephone, String authCode);

    /**
     * 获取验证码
     */
    String getAuthCode(String telephone);
    /**
     * 获取客户缓存
     */
    UmsMember getMember(Long id);

    /**
     * 设置客户缓存
     */
    void setMember(UmsMember member);

}
