package com.cloud.tv.core.service;

import com.cloud.tv.dto.MonitorDto;
import com.cloud.tv.entity.Monitor;
import com.cloud.tv.vo.MonitorVo;
import com.github.pagehelper.Page;

public interface IMonitorService {
    Monitor getObjById(Long id);

    Monitor getObjBySign(String sign);

    Page<MonitorVo> query(MonitorDto dto);

    Page<MonitorVo> querySchool(MonitorDto dto);

    boolean save(MonitorDto instance);

    boolean update(MonitorDto instance);

    int delete(Long id);
}
