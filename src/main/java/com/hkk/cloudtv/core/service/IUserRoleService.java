package com.hkk.cloudtv.core.service;

import com.hkk.cloudtv.entity.Role;
import com.hkk.cloudtv.entity.UserRole;

import java.util.List;

public interface IUserRoleService {

    /**
     * 批量添加角色
     * @param userRoles
     * @return
     */
    public int batchAddUserRole(List<UserRole> userRoles);
}
