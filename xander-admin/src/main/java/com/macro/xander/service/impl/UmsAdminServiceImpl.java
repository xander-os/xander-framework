package com.macro.xander.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.macro.xander.common.api.CommonResult;
import com.macro.xander.common.api.ResultCode;
import com.macro.xander.common.constant.AuthConstant;
import com.macro.xander.common.domain.UserDto;
import com.macro.xander.common.exception.Asserts;
import com.macro.xander.dao.UmsAdminRoleRelationDao;
import com.macro.xander.dto.UmsAdminParam;
import com.macro.xander.dto.UpdateAdminPasswordParam;
import com.macro.xander.mapper.UmsAdminMapper;
import com.macro.xander.mapper.UmsAdminRoleRelationMapper;
import com.macro.xander.model.*;
import com.macro.xander.service.AuthService;
import com.macro.xander.service.UmsAdminCacheService;
import com.macro.xander.service.UmsAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;
    @Autowired
    private HttpServletRequest request;

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

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample umsAdminExample = new UmsAdminExample();
        umsAdminExample.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> umsAdmins = umsAdminMapper.selectByExample(umsAdminExample);
        if (CollectionUtil.size(umsAdmins) > 0){
            return umsAdmins.get(0);
        }
        return null;
    }

    @Override
    public UmsAdmin getAdminById(Long id) {
        return umsAdminMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(keyword)){
            criteria.andUsernameLike("%" + keyword + "%");
            example.or(example.createCriteria().andNickNameLike("%" + keyword + "%"));
        }
        return umsAdminMapper.selectByExample(example);
    }

    @Override
    public int update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = umsAdminMapper.selectByPrimaryKey(id);
        if(rawAdmin.getPassword().equals(admin.getPassword())){
            //与原加密密码相同的不需要修改
            admin.setPassword(null);
        }else{
            //与原加密密码不同的需要加密修改
            if(StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else{
                admin.setPassword(BCrypt.hashpw(admin.getPassword()));
            }
        }
        int count = umsAdminMapper.updateByPrimaryKeySelective(admin);
        // 清掉缓存
        getCacheService().delAdmin(id);
        return count;
    }

    @Override
    public int delete(Long id) {
        // 硬删除
        int count = umsAdminMapper.deleteByPrimaryKey(id);
        getCacheService().delAdmin(id);
        return count;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的关系
        UmsAdminRoleRelationExample adminRoleRelationExample = new UmsAdminRoleRelationExample();
        adminRoleRelationExample.createCriteria().andAdminIdEqualTo(adminId);
        adminRoleRelationMapper.deleteByExample(adminRoleRelationExample);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationDao.insertList(list);
        }
        return count;
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        return adminRoleRelationDao.getResourceList(adminId);
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                ||StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(param.getUsername());
        List<UmsAdmin> adminList = umsAdminMapper.selectByExample(example);
        if(CollUtil.isEmpty(adminList)){
            return -2;
        }
        UmsAdmin umsAdmin = adminList.get(0);
        if(!BCrypt.checkpw(param.getOldPassword(),umsAdmin.getPassword())){
            return -3;
        }
        umsAdmin.setPassword(BCrypt.hashpw(param.getNewPassword()));
        umsAdminMapper.updateByPrimaryKey(umsAdmin);
        getCacheService().delAdmin(umsAdmin.getId());
        return 1;
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        //获取用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsRole> roleList = getRoleList(admin.getId());
            UserDto userDTO = new UserDto();
            BeanUtils.copyProperties(admin,userDTO);
            if(CollUtil.isNotEmpty(roleList)){
                List<String> roleStrList = roleList.stream().map(item -> item.getId() + "_" + item.getName()).collect(Collectors.toList());
                userDTO.setRoles(roleStrList);
            }
            return userDTO;
        }
        return null;
    }

    @Override
    public UmsAdmin getCurrentAdmin() {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if(StrUtil.isEmpty(userStr)){
            Asserts.failed(ResultCode.UNAUTHORIZED);
        }
        UserDto userDto = JSONUtil.toBean(userStr, UserDto.class);
        UmsAdmin admin = getCacheService().getAdmin(userDto.getId());
        if(admin!=null){
            return admin;
        }else{
            admin = umsAdminMapper.selectByPrimaryKey(userDto.getId());
            getCacheService().setAdmin(admin);
            return admin;
        }
    }

    @Override
    public UmsAdminCacheService getCacheService() {
        // 也可以注入@Autowired,直接return
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return adminRoleRelationDao.getRoleList(adminId);
    }
}
