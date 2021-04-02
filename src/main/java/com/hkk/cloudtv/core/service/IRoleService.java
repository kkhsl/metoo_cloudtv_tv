package com.hkk.cloudtv.core.service;

import com.hkk.cloudtv.entity.Res;
import com.hkk.cloudtv.entity.Role;

import java.util.List;

public interface IRoleService {

    /**
     * 根据角色id查询指定角色
     * @param id
     * @return
     */
    List<Role> findRoleById(Long id);

    List<Role> findRoleByType(String type);

    /**
     *根据用户id查询用所有角色信息
     * @param user_id
     * @return
     */
    List<Role> findRoleByUserId(Long user_id);
}
