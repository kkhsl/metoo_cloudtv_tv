package com.hkk.cloudtv.core.service;

import com.hkk.cloudtv.entity.User;

public interface IRegisterService {

    /**
     * 注册用户
     *
     * @param user
     */
    default int register(User user) {
        return 0;
    }


    User findByUsername(String username);

    /**
     * 根据用户名查询所有角色
     * @param username
     * @return
     */
    User findRolesByUserName(String username);
}
