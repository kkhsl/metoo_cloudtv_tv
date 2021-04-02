package com.hkk.cloudtv.core.service.impl;

import com.hkk.cloudtv.core.mapper.SysConfigMapper;
import com.hkk.cloudtv.core.service.ISysConfigService;
import com.hkk.cloudtv.entity.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysConfigServiceImpl implements ISysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public List<SysConfig> findSysConfigList() {
        return this.sysConfigMapper.select();
    }

    @Override
    public int modify(SysConfig instance) {
        return this.sysConfigMapper.update(instance);
    }
}
