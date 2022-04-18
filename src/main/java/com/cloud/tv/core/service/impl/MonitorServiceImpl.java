package com.cloud.tv.core.service.impl;

import com.cloud.tv.core.manager.admin.tools.MonitorTools;
import com.cloud.tv.core.mapper.MonitorMapper;
import com.cloud.tv.core.service.IMonitorService;
import com.cloud.tv.dto.MonitorDto;
import com.cloud.tv.entity.Monitor;
import com.cloud.tv.vo.MonitorVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MonitorServiceImpl implements IMonitorService {

    @Autowired
    private MonitorTools monitorTools;
    @Autowired
    private MonitorMapper monitorMapper;

    public MonitorServiceImpl() {
    }

    public Monitor getObjById(Long id) {
        return this.monitorMapper.getObjById(id);
    }

    public Monitor getObjBySign(String sign) {
        return this.monitorMapper.getObjBySign(sign);
    }

    public Page<MonitorVo> query(MonitorDto dto) {
        Page<MonitorVo> page = PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        this.monitorMapper.query(dto);
        return page;
    }

    public Page<MonitorVo> querySchool(MonitorDto dto) {
        Page<MonitorVo> page = PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        this.monitorMapper.querySchool(dto);
        return page;
    }

    public boolean save(MonitorDto instance) {
        Monitor monitor = this.monitorMapper.getObjBySign(instance.getSign());
        if (monitor == null) {
            monitor = new Monitor();
            monitor.setAddTime(new Date());
            monitor.setBeginTime(new Date());
            BeanUtils.copyProperties(instance, monitor);
        } else {
            monitor.setEndTime(new Date());
        }

        if (monitor.getId() == null) {
            try {
                monitor.setStatus(1);
                this.monitorMapper.save(monitor);
                return true;
            } catch (Exception var4) {
                var4.printStackTrace();
                return false;
            }
        } else {
            try {
                monitor.setStatus(0);
                Long duration = this.monitorTools.duration(monitor.getBeginTime(), monitor.getEndTime());
                monitor.setDuration(duration);
                this.monitorMapper.update(monitor);
                return true;
            } catch (Exception var5) {
                var5.printStackTrace();
                return false;
            }
        }
    }

    public boolean update(MonitorDto instance) {
        Monitor monitor = new Monitor();
        BeanUtils.copyProperties(instance, monitor);

        try {
            this.monitorMapper.update(monitor);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public int delete(Long id) {
        return this.monitorMapper.delete(id);
    }
}