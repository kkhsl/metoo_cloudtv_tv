package com.hkk.cloudtv.core.service.impl;

import com.hkk.cloudtv.core.mapper.RoleMapper;
import com.hkk.cloudtv.core.service.IRoleService;
import com.hkk.cloudtv.entity.Res;
import com.hkk.cloudtv.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("roleService")
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRoleById(Long id) {
        return this.roleMapper.findRoleById(id);
    }

    @Override
    public List<Role> findRoleByType(String type) {
        return this.roleMapper.findRoleByType(type);
    }

    @Override
    public List<Role> findRoleByUserId(Long user_id) {
        return this.roleMapper.findRoleByUserId(user_id);
    }
}
