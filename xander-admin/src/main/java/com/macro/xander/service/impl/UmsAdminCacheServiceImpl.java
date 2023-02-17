package com.macro.xander.service.impl;

import com.macro.xander.common.service.RedisService;
import com.macro.xander.model.UmsAdmin;
import com.macro.xander.service.UmsAdminCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/17 17:38
 * @email ：zhrunxin33@gmail.com
 * @description：后台用户缓存实现类
 */
@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {

    @Autowired
    private RedisService redisService;

    /**
     * admin用户缓存配置
     */
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    @Override
    public void delAdmin(Long adminId) {
        String key = REDIS_DATABASE + ":" +REDIS_KEY_ADMIN + ":" + adminId;
        redisService.del(key);
    }

    @Override
    public UmsAdmin getAdmin(Long adminId) {
        String key = REDIS_DATABASE + ":" +REDIS_KEY_ADMIN + ":" + adminId;
        return (UmsAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(UmsAdmin umsAdmin) {
        String key = REDIS_DATABASE + ":" +REDIS_KEY_ADMIN + ":" + umsAdmin.getId();
        redisService.set(key,umsAdmin,REDIS_EXPIRE);
    }
}
