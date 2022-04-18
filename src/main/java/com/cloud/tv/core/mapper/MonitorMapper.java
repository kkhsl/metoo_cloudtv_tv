package com.cloud.tv.core.mapper;

import com.cloud.tv.dto.MonitorDto;
import com.cloud.tv.entity.Monitor;
import com.cloud.tv.vo.MonitorVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MonitorMapper {
    Monitor getObjById(Long id);

    Monitor getObjBySign(String sign);

    List<MonitorVo> query(MonitorDto dto);

    List<MonitorVo> querySchool(MonitorDto dto);

    int save(Monitor instance);

    int update(Monitor instance);

    int delete(Long id);
}
