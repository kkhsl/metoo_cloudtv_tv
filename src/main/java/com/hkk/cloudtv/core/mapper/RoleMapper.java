package com.hkk.cloudtv.core.mapper;

import com.hkk.cloudtv.entity.Res;
import com.hkk.cloudtv.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {

    /**
     * 根据角色id查询指定角色
     * @param id
     * @return
     */
    List<Role> findRoleById(Long id);

    /**
     * 根据角色类型，查询所有角色
     * @param type
     * @return
     */
    List<Role> findRoleByType(String type);


    /**
     *根据用户id查询用所有角色信息
     * @param user_id
     * @return
     */
    List<Role> findRoleByUserId(Long user_id);


}
