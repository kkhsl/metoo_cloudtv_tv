package com.cloud.tv.core.service;

import com.cloud.tv.dto.PermissionDto;
import com.cloud.tv.entity.Res;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface IResService {

    /**
     * 根据角色id查询权限集合
     * @param id
     * @return
     */
    List<Res> findResByRoleId(Long id);

    /**
     * 根据权限ID查询权限对象
     * @param id
     * @return
     */
    Res findObjById(Long id);

    Res findResUnitRoleByResId(Long id);

    Page<Res> query(Map<String, Integer> params);

    List<Res> findResByResIds(List<Integer> ids);

    boolean save(PermissionDto instance);

    boolean delete(Long id);

}
