package com.cloud.tv.core.mapper;

import com.cloud.tv.entity.Res;
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


    Res  findResUnitRoleByResId(Long id);

    /**
     * 根据权限ID查询权限对象
     * @param id
     * @return
     */
    Res selectPrimaryById(Long id);



    List<Res> findResByAll();

    List<Res> findResByResIds(List<Integer> ids);

    /**
     * 保存一个系统资源
     * @param instance
     * @return
     */
    int save(Res instance);

    /**
     * 更新一个系统资源对象
     * @param instance
     * @return
     */
    int update(Res instance);

    /**
     * 根据系统资源ID删除一个系统资源对象
     * @param id
     * @return
     */
    int delete(Long id);

}
