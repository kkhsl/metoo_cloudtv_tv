package com.hkk.cloudtv.core.mapper;

import com.hkk.cloudtv.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysConfigMapper {

    /**
     * 查询所有 配置
     * @return
     */
    List<SysConfig> select();

    /**
     * 更新
     * @param instance
     * @return
     */
    int update(SysConfig instance);
}
