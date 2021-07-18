package com.cloud.tv.core.service;

import com.cloud.tv.dto.RoleDto;
import com.cloud.tv.entity.Role;
import com.cloud.tv.vo.RoleVo;

import java.util.List;
import java.util.Map;

public interface IRoleService {

    /**
     * 根据角色id查询指定角色
     * @param id
     * @return
     */
    Role findRoleById(Long id);

    List<Role> findRoleByType(String type);

    boolean countBy(String name);

    /**
     *根据用户id查询用所有角色信息
     * @param user_id
     * @return
     */
    List<Role> findRoleByUserId(Long user_id);

    List<Role> findRoleByRoleGroupId(Long role_group_id);

    List<Role> findRoleByResId(Long res_id);

    List<Role> findRoleByIdList(List<Integer> list);

    Role selectByPrimaryUpdae(Long id);

    boolean save(RoleDto instance);

    Object update(RoleDto instance);

    List<Role> query(Map params);

    List<RoleVo> queryRole(Map params);

    boolean delete(Long id);

    int batchUpdateRoleGroupId(Map map);
}
