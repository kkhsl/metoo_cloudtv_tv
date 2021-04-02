package com.hkk.cloudtv.core.mapper;

import com.hkk.cloudtv.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BuyerMapper {


    /**
     * 保存一个User对象
     * @param user
     */
    void save(User user);
    /**
     * 根据Username 查询一个User 对象
     * @param username
     * @return
     */
    User findByUserName(String username);

    /**
     * 根据用户名查询所有角色
     * @param username
     * @return
     */
    User findRolesByUserName(String username);


}
