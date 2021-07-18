package com.cloud.tv.core.service;

import com.cloud.tv.dto.UserDto;
import com.cloud.tv.entity.User;
import com.cloud.tv.vo.UserVo;
import com.github.pagehelper.Page;

import java.util.Map;

public interface IUserService {

    /**
     * 根据Username 查询一个User 对象
     * @param username
     * @return
     */
    User findByUserName(String username);

    User findRolesByUserName(String username);

    UserVo findUserUpdate(Long id);

    User findObjById(Long id);

    Page<UserVo> query(Map params);

    boolean save(UserDto dto);

    boolean update(User user);

    boolean delete(User id);
}
