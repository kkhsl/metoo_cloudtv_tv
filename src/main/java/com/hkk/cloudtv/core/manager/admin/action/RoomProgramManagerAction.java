package com.hkk.cloudtv.core.manager.admin.action;

import com.hkk.cloudtv.core.service.IRoomProgramService;
import com.hkk.cloudtv.entity.RoomProgram;
import com.hkk.cloudtv.vo.Result;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * Description: 直播节目管理控制器；负责创建直播节目，多个节目可对应一个直播间，也可一个直播对应一个直播间；两种模式；
 * </p>
 */
@RestController
@RequestMapping("/admin/program")
public class RoomProgramManagerAction {

    @Autowired
    private IRoomProgramService roomProgramService;

    @GetMapping("/list")
    @RequiresPermissions("ADMIN_PROGRAM_LIST")
    public Object program(HttpServletRequest request, HttpServletResponse response, Integer currentPage, Integer pageSize) {
        Map map = new HashMap();
        if (currentPage == null || currentPage.equals("")) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize.equals("")) {
            pageSize = 15;
        }
        int totalRow = this.roomProgramService.getAccountByTotal();// 获取总数
        int tatolPages = totalRow / currentPage;
        int left = totalRow % pageSize;
        if (left > 0) {
            tatolPages += 1;
        }
        int startRow = (currentPage - 1) * pageSize;// 起始行
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startRow", startRow);
        params.put("pageSize", pageSize);
        List<RoomProgram> roomProgramList = this.roomProgramService.getRoomProgram(params);
        map.put("pageSize", pageSize);
        map.put("currentPage", currentPage);
        map.put("list", roomProgramList);
        return new Result(200, "Successfully", map);
    }

    /**
     * 直播节目添加
     * @param request
     * @param response
     * @param roomprogram
     * @return
     */
    @RequestMapping("/save")
    @RequiresPermissions("ADMIN_PROGRAM_SAVE")
    public Object save(HttpServletRequest request, HttpServletResponse response, RoomProgram roomprogram) {
        return this.roomProgramService.save(roomprogram);
    }

    @PutMapping("/update")
    @RequiresPermissions("ADMIN_PROGRAM_UPDATE")
    public Object update(RoomProgram instance){
        return this.roomProgramService.update(instance);
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("ADMIN_PROGRAM_DELETE")
    public Object delete(Integer id){
        if(id != null && !id.equals("")){
            try {
                int p = 9/0;
                this.roomProgramService.delete(Long.valueOf(id));
                return new Result(200, "Successfully");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(500, e.getMessage());
            }
        }else{
            return new Result(400, "Parameter is null");
        }
    }

    /**
     * 创建直播节目-默认创建直播间
     * @param response
     * @param request
     * @param roomProgram
     * @return
     */
    @RequestMapping("/liveroom")
    @RequiresPermissions("ADMIN_PROGRAM_LIVEROOM")
    public Object ProgramLiveRoom(HttpServletResponse response, HttpServletRequest request, RoomProgram roomProgram){
        return this.roomProgramService.programLiveRoom(roomProgram);
    }
}
