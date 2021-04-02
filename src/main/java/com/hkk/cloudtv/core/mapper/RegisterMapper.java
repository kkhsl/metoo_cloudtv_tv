package com.hkk.cloudtv.core.mapper;

import com.hkk.cloudtv.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegisterMapper {

    /**
     * 保存一个User对象
     * @param user
     */
    int save(User user);


}
