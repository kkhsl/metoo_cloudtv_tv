package com.hkk.cloudtv.core.mapper;

import com.hkk.cloudtv.entity.Role;
import com.hkk.cloudtv.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    /**
     * 批量添加角色
     * @param userRoles
     * @return
     */
    public int batchAddUserRole(List<UserRole> userRoles);
}
