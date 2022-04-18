package com.cloud.tv.core.manager.admin.action;

import com.cloud.tv.core.manager.admin.tools.MonitorTools;
import com.cloud.tv.core.service.*;
import com.cloud.tv.core.utils.CommUtils;
import com.cloud.tv.core.utils.query.PageInfo;
import com.cloud.tv.dto.MonitorDto;
import com.cloud.tv.entity.*;
import com.cloud.tv.core.service.*;
import com.cloud.tv.core.manager.admin.tools.PlayBackTools;
import com.cloud.tv.core.manager.admin.tools.ShiroUserHolder;
import com.cloud.tv.core.utils.FileUtil;
import com.cloud.tv.core.utils.ResponseUtil;
import com.cloud.tv.dto.RoomProgramDto;
import com.cloud.tv.vo.Result;
import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: RoomProgramManagerAction.java
 * </p>
 *
 * <p>
 * Description: 直播节目管理控制器；负责创建直播节目，多个节目可对应一个直播间
 * </p>
 */
@ApiOperation("直播管理")
@RestController
@RequestMapping("/admin/program")
public class RoomProgramManagerController {
    @Autowired
    private IRoomProgramService roomProgramService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ILiveRoomService liveRoomService;
    @Autowired
    private PlayBackTools playBackTools;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private MonitorTools monitorTools;

    public RoomProgramManagerController() {
    }

    @RequiresPermissions({"LK:ROOMPROGRAM"})
    @ApiOperation("直播列表")
    @RequestMapping({"/list"})
    public Object list(@RequestBody RoomProgramDto dto) {
        User user = ShiroUserHolder.currentUser();
        if (dto == null) {
            dto = new RoomProgramDto();
        }

        dto.setUserId(user.getId());
        Page<RoomProgram> page = this.roomProgramService.query(dto);
        return page.getResult().size() > 0 ? ResponseUtil.ok(new PageInfo(page)) : ResponseUtil.ok();
    }

    @RequiresPermissions({"LK:ROOMPROGRAM"})
    @ApiOperation("直播添加")
    @RequestMapping({"/add"})
    public Object add() {
        User user = ShiroUserHolder.currentUser();
        Map map = new HashMap();
        Map<String, Object> params = new HashMap();
        params.put("currentPage", 1);
        params.put("pageSize", 15);
        params.put("display", 1);
        List<Course> courseList = this.courseService.findBycondition(params);
        List<Grade> grades = this.gradeService.findBycondition(params);
        map.put("courseList", courseList);
        map.put("gradeList", grades);
        return ResponseUtil.ok(map);
    }

    @RequiresPermissions({"LK:ROOMPROGRAM"})
    @ApiOperation("直播更新")
    @RequestMapping({"/update"})
    public Object update(@RequestBody RoomProgramDto dto) {
        Map map = new HashMap();
        Map<String, Object> params = new HashMap();
        RoomProgram roomProgram = this.roomProgramService.findObjById(dto.getId());
        if (roomProgram != null) {
            map.put("obj", roomProgram);
            if (roomProgram.getRoomId() != null) {
                LiveRoom liveRoom = this.liveRoomService.getObjById(roomProgram.getRoomId());
                map.put("liveRoom", liveRoom);
            }

            params.clear();
            params.put("currentPage", 1);
            params.put("pageSize", 15);
            params.put("display", 1);
            List<Course> courseList = this.courseService.findBycondition(params);
            List<Grade> grades = this.gradeService.findBycondition(params);
            map.put("courseList", courseList);
            map.put("gradeList", grades);
            return ResponseUtil.ok(map);
        } else {
            return ResponseUtil.badArgument();
        }
    }

    @RequiresPermissions({"LK:ROOMPROGRAM"})
    @ApiOperation("直播保存")
    @RequestMapping({"/save"})
    public Object save(@RequestBody RoomProgramDto dto) {
        if (dto != null) {
            return this.roomProgramService.save(dto) ? ResponseUtil.ok() : ResponseUtil.error();
        } else {
            return ResponseUtil.badArgument();
        }
    }

    @RequiresPermissions({"LK:ROOMPROGRAM"})
    @RequestMapping({"/delete"})
    public Object delete(@RequestBody RoomProgramDto dto) {
        if (dto.getId() != null && !dto.getId().equals("")) {
            try {
                this.roomProgramService.delete(dto.getId());
                return new Result(200, "Success");
            } catch (Exception var3) {
                var3.printStackTrace();
                return new Result(500, var3.getMessage());
            }
        } else {
            return new Result(400, "Parameter is null");
        }
    }

    @ApiOperation("直播")
    @RequestMapping(
            value = {"/liveroom"},
            method = {RequestMethod.POST}
    )
    public Object ProgramLiveRoom(HttpServletResponse response, HttpServletRequest request, @RequestBody(required = false) RoomProgram roomProgram) {
        return this.roomProgramService.programLiveRoom(roomProgram);
    }

    @RequiresPermissions({"LK:ROOMPROGRAM"})
    @ApiOperation("直播On/Off")
    @RequestMapping({"/change"})
    public Object changeRoomProgram(@RequestBody RoomProgramDto dto) throws IOException {
        RoomProgram obj = this.roomProgramService.findObjById(dto.getId());
        User user = ShiroUserHolder.currentUser();
        if (obj != null && obj.getUserId().equals(user.getId())) {
            if (dto.getIsPlayback() != null) {
                obj.setIsPlayback(obj.getIsPlayback() == 0 ? 1 : 0);
                return this.roomProgramService.update(obj) ? ResponseUtil.ok() : ResponseUtil.error();
            } else {
                String path = null;
                SysConfig sysconfig = this.sysConfigService.findSysConfigList();
                LiveRoom liveRoom = this.liveRoomService.getObjById(obj.getRoomId());
                if (liveRoom.getIsEnable() != 1) {
                    return ResponseUtil.badArgument("请先开启直播间");
                } else if (liveRoom == null) {
                    return ResponseUtil.badArgument("未关联直播间");
                } else {
                    if (liveRoom.getBindCode() != null) {
                        path = sysconfig.getPath() + File.separator + liveRoom.getBindCode();
                    }

                    String timestamp = "";
                    String appId;
                    if (obj.getStatus() == 1) {
                        timestamp = obj.getTimestamp();
                        obj.setStatus(0);
                        if (obj.getIsPlayback() == 1) {
                            boolean flag = this.playBackTools.create(liveRoom.getId(), obj, liveRoom.getBindCode());
                            if (flag) {
                                obj.setPlayback(1);
                            }
                        } else {
                            path = sysconfig.getPath() + File.separator + liveRoom.getBindCode();
                            FileUtil.delFileTs(path);
                            appId = path + File.separator + "index.m3u8";
                            FileUtil.delFile(appId);
                        }

                        try {
                            this.roomProgramService.update(obj);
                            liveRoom.setLive(0);
                            liveRoom.setRoomProgramId((Long)null);
                            this.liveRoomService.update(liveRoom);
                        } catch (Exception var16) {
                            var16.printStackTrace();
                        }

                        if (path != null) {
                            FileUtil.storeFile(path);
                        }
                    } else {
                        Map params = new HashMap();
                        params.put("roomId", liveRoom.getId());
                        params.put("status", 1);
                        List<RoomProgram> roomPrograms = this.roomProgramService.findObjByCondition(params);
                        if (roomPrograms != null && roomPrograms.size() > 0) {
                            return new Result(202, "当前直播间正在播放: " + ((RoomProgram)roomPrograms.get(0)).getTitle());
                        }

                        obj.setStatus(1);
                        long current_time_millis = System.currentTimeMillis();
                        long timeStampSec = System.currentTimeMillis() / 1000L;
                        timestamp = String.format("%010d", timeStampSec);
                        obj.setTimestamp(timestamp);

                        try {
                            this.roomProgramService.update(obj);
                            liveRoom.setLive(1);
                            liveRoom.setLastTime(new Date());
                            liveRoom.setRoomProgramId(obj.getId());
                            this.liveRoomService.update(liveRoom);
                        } catch (Exception var15) {
                            var15.printStackTrace();
                        }

                        if (path != null) {
                            FileUtil.storeFileOpen(path);
                        }
                    }

                    appId = sysconfig.getAppId();
                    if (appId == null || appId.equals("")) {
                        appId = this.monitorTools.createAppId();
                        sysconfig.setAppId(appId);
                        this.configService.update(sysconfig);
                    }

                    this.monitorTools.monitor(timestamp, sysconfig.getTitle(), obj.getTitle(), liveRoom.getTitle(), appId, user.getUsername(), liveRoom.getId(), obj.getId(), user.getId());
                    return new Result(200, "Success");
                }
            }
        } else {
            return new Result(400, "Param error");
        }
    }

    @RequiresPermissions({"LK:ROOMPROGRAM:MANAGER"})
    @ApiModelProperty("直播节目管理")
    @RequestMapping({"/manager/list"})
    public Object managerList(@RequestBody(required = false) RoomProgramDto dto) {
        new HashMap();
        Page<RoomProgram> page = this.roomProgramService.query(dto);
        return page.getResult().size() > 0 ? ResponseUtil.ok(new PageInfo(page)) : ResponseUtil.ok();
    }

    @RequiresPermissions({"LK:ROOMPROGRAM:MANAGER"})
    @ApiOperation("直播管理更新")
    @RequestMapping({"/manager/update"})
    public Object managerUpdate(@RequestBody(required = false) RoomProgramDto dto) {
        RoomProgram roomProgarm = this.roomProgramService.findObjById(dto.getId());
        if (roomProgarm != null) {
            Map map = new HashMap();
            map.put("obj", roomProgarm);
            Map params = new HashMap();
            params.put("currentPage", 1);
            params.put("pageSize", 1);
            params.put("display", 1);
            return ResponseUtil.ok(map);
        } else {
            return ResponseUtil.badArgument("未找到相关资源");
        }
    }

    @RequiresPermissions({"LK:ROOMPROGRAM:MANAGER"})
    @ApiOperation("平台直播管理保存")
    @RequestMapping({"/manager/save"})
    public Object managerSave(@RequestBody(required = false) RoomProgramDto dto) {
        if (dto != null) {
            RoomProgram roomProgram = this.roomProgramService.findObjById(dto.getId());
            if (roomProgram != null) {
                if (dto.getStatus() != null) {
                    int status = roomProgram.getStatus() == 1 ? 0 : 1;
                    roomProgram.setStatus(status);
                    if (this.roomProgramService.update(roomProgram)) {
                        return ResponseUtil.ok();
                    }

                    return ResponseUtil.error();
                }

                return ResponseUtil.ok();
            }
        }

        return ResponseUtil.badArgument("未找到相关资源");
    }
}
