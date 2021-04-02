package com.hkk.cloudtv.core.service.impl;

import com.hkk.cloudtv.core.mapper.UserRoleMapper;
import com.hkk.cloudtv.core.service.IUserRoleService;
import com.hkk.cloudtv.entity.Role;
import com.hkk.cloudtv.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserRoleServiceImpl implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public int batchAddUserRole(List<UserRole> userRoles) {
        return this.userRoleMapper.batchAddUserRole(userRoles);
    }
}
