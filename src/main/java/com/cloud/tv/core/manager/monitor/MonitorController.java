package com.cloud.tv.core.manager.monitor;

import com.cloud.tv.core.manager.admin.tools.ShiroUserHolder;
import com.cloud.tv.core.service.IMonitorService;
import com.cloud.tv.core.utils.ResponseUtil;
import com.cloud.tv.core.utils.query.PageInfo;
import com.cloud.tv.dto.MonitorDto;
import com.cloud.tv.entity.User;
import com.cloud.tv.vo.MonitorVo;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("监控管理器")
@RestController
@RequestMapping({"/monitor"})
public class MonitorController {
    @Autowired
    private IMonitorService monitorService;

    public MonitorController() {
    }

    @ApiOperation("监控列表")
    @RequestMapping({"/school/list"})
    public Object school(@RequestBody(required = false) MonitorDto dto) {
        if (dto == null) {
            dto = new MonitorDto();
        }

        User user = ShiroUserHolder.currentUser();
        Page<MonitorVo> page = this.monitorService.querySchool(dto);
        return page.getResult().size() > 0 ? ResponseUtil.ok(new PageInfo(page)) : ResponseUtil.ok();
    }

    private static ArrayList<MonitorVo> removeDuplicateUser(List<MonitorVo> monitorVo) {
        Set<MonitorVo> set = new TreeSet(new Comparator<MonitorVo>() {
            public int compare(MonitorVo o1, MonitorVo o2) {
                return o1.getAppId().compareTo(o2.getAppId());
            }
        });
        set.addAll(monitorVo);
        return new ArrayList(set);
    }

    @ApiOperation("监控列表")
    @RequestMapping({"/list"})
    public Object mointor(@RequestBody MonitorDto dto) {
        if (dto == null) {
            dto = new MonitorDto();
        }

        User user = ShiroUserHolder.currentUser();
        Page<MonitorVo> page = this.monitorService.query(dto);
        return page.getResult().size() > 0 ? ResponseUtil.ok(new PageInfo(page)) : ResponseUtil.ok();
    }

    @ApiOperation("监控日志-添加")
    @RequestMapping({"/save"})
    public Object save(@RequestBody MonitorDto dto) {
        if (dto != null) {
            this.monitorService.save(dto);
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.badArgument();
        }
    }
}
