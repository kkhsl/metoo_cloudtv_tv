package com.hkk.cloudtv.core.manager.admin.action;

import com.hkk.cloudtv.core.service.ILiveRoomService;
import com.hkk.cloudtv.core.utils.CommUtils;
import com.hkk.cloudtv.entity.LiveRoom;
import com.hkk.cloudtv.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Title：LiveRoomManagerAction.java
 * </p>
 *
 * <p>
 *  Description: 直播间管理控制器；
 * </p>
 *
 * <p>
 *  author: pers hkk
 * </p>
 */
@RestController
@RequestMapping(value = "/admin/room/")
public class LiveRoomManagerAction {

    @Autowired
    private ILiveRoomService liveRoomService;

    /**
     * 直播间列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "list")
    @RequiresPermissions("ADMIN_ROOM_LIST")
    public Object rooms(HttpServletRequest request, HttpServletResponse response, Integer currentPage, Integer pageSize) {
        Map map = new HashMap();
        // 分页查询
        if(currentPage == null || currentPage.equals("")){
            currentPage = 1;
        }
        if(pageSize == null || pageSize.equals("")){
            pageSize = 2;
        }
        int totalRows = this.liveRoomService.findAccountByTotal();// 查询总数
        int totalPages = totalRows / pageSize;// 总页数
        int left = totalRows % pageSize;
        if(left > 0){
            totalPages += 1;
        }
        // 判断用户是否手动修改页码
        if(currentPage > totalPages){
            currentPage = totalPages;
        }
        // 起始行
        int startRow = (currentPage - 1) * pageSize;
        // 封装分页参数
        Map<String ,Object> params = new HashMap();
        params.put("startRow", startRow);
        params.put("pageSize", pageSize);
        List<LiveRoom> liveRooms = this.liveRoomService.findByPager(params);
        map.put("totalPages", totalPages);
        map.put("pageSize", liveRooms.size());
        map.put("list", liveRooms);

        //List<LiveRoom> liveRooms = this.liveRoomService.findAllLiveRoom();
        return new Result(200, "Successfully", map);
    }

    /**
     * 创建直播间
     * @param request
     * @param response
     * @param room
     * @return
     */
    @RequestMapping(value = "save")
    @RequiresPermissions("ADMIN_ROOM_SAVE")
    public Object creatRoot(HttpServletRequest request, HttpServletResponse response, LiveRoom room) {
        if (room != null) {
            room.setAddTime(new Date());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String df = sdf.format(date);
            String bindCode = df + CommUtils.randomString(6);// 推流码
            room.setBindCode(bindCode);
            int flag = this.liveRoomService.save(room);
            if(flag == 1){
                return new Result(200, "Successfully");
            }
        }
        return new Result(400,"Parameter Error");
    }

    /**
     * 删除直播间
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping(value = "delete")
    @RequiresPermissions("ADMIN_ROOM_DELETE")
    public Object delete(HttpServletRequest request, HttpServletResponse response, Integer id ){
        if(id != null || !id.equals("")){
            int flag = this.liveRoomService.delete(Long.valueOf(id));
            if(flag == 1){
                return new Result(200, "Succesfully");
            }else{
                return new Result(500, "Delete Error");
            }
        }else{
            return new Result(400, "Parameter error");
        }
    }

    /**
     * 更新直播间
     * @param room
     * @return
     */
    @RequestMapping("update")
    @RequiresPermissions("ADMIN_ROOM_UPDATE")
    public Object update(LiveRoom room){
        try {
            this.liveRoomService.update(room);
            return new Result(200, "Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(500, "Update error");
        }
    }

    /**
     * 直播间 on/off
     * @return
     */
    @PutMapping("/change")
    @RequiresPermissions("ADMIN_ROOM_CHANGE")
    public Object changeLiveRoom(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("property");
        LiveRoom liveRoom = this.liveRoomService.getObjById(Long.valueOf(id));
        if (liveRoom != null) {
            try {
                if (liveRoom.getIsEnable() == 0) {
                    liveRoom.setIsEnable(1);
                } else {
                    liveRoom.setIsEnable(0);
                }
                this.liveRoomService.update(liveRoom);
                return new Result(200, "Successfully");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(500, "update Error");
            }
        }else{
            return new Result(400, "Params error");
        }
    }

    /**
     * pageHelper 分页
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/allLiveRoom")
    public Object queryAllLiveRoom(Integer currentPage, Integer pageSize){
        Map map = new HashMap();
        if(currentPage == null || pageSize.equals("")){
            currentPage = 1;
        }
        if(pageSize == null || pageSize.equals("")){
            pageSize = 15;
        }
        List<LiveRoom> liveRoomList = this.liveRoomService.queryLiveRooms(currentPage, pageSize);
        map.put("currentPage", currentPage);
        map.put("pageSize", liveRoomList.size());
        map.put("liveRoomList", liveRoomList);
        return new Result(200, "Successfully", map);
    }
}
