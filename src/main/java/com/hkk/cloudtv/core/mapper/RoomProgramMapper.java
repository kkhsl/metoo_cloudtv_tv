package com.hkk.cloudtv.core.mapper;

import com.hkk.cloudtv.entity.RoomProgram;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomProgramMapper {


    /**
     * Limit分页查询
     * @param params
     * @return
     */
    List<RoomProgram> findObjByPage(Map<String, Object> params);

    /**
     * 查询总数
     * @return
     */
    int findAccountByTotal();

    /**
     * 创建一个RoomProgram对象
     * @param instance
     * @return
     */
    int insert(RoomProgram instance);

    /**
     * 更新一个RoomProgram对象
     * @param instance
     * @return
     */
    int update(RoomProgram instance);

    /**
     * 删除一个RoomProgram对象
     * @param id
     * @return
     */
    int delete(Long id);


}
