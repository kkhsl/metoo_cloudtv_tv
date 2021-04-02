package com.hkk.cloudtv.core.mapper;

import com.hkk.cloudtv.entity.Res;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResMapper {

    /**
     * 根据角色id查询权限集合
     * @param id
     * @return
     */
    List<Res> findResByRoleId(Long id);

}
