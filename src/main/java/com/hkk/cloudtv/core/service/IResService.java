package com.hkk.cloudtv.core.service;

import com.hkk.cloudtv.entity.Res;

import java.util.List;

public interface IResService {

    /**
     * 根据角色id查询权限集合
     * @param id
     * @return
     */
    List<Res> findResByRoleId(Long id);

}
