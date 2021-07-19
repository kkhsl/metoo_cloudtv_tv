package com.cloud.tv.core.manager.admin.action;

import com.cloud.tv.core.manager.admin.tools.ShiroUserHolder;
import com.cloud.tv.core.service.*;
import com.cloud.tv.core.utils.query.PageInfo;
import com.cloud.tv.dto.VideoDto;
import com.cloud.tv.entity.*;
import com.cloud.tv.req.VideoReq;
import com.cloud.tv.vo.Result;
import com.github.pagehelper.Page;
import com.cloud.tv.core.service.*;
import com.cloud.tv.entity.*;
import com.cloud.tv.core.service.*;
import com.cloud.tv.core.utils.ResponseUtil;
import com.cloud.tv.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Api("视频管理控制器")
@RestController
@RequestMapping("/admin/video")
public class VideoManagerController {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IAccessoryService accessoryService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IRoomProgramService roomProgramService;
    @Autowired
    private ILiveRoomService liveRoomService;

    @RequiresPermissions("ADMIN:VIDEO:LIST")
    @ApiOperation("视频列表")
    @PostMapping("/list")
    public Object list(@RequestBody(required=false) VideoDto dto){
        User user = ShiroUserHolder.currentUser();
        Map data = new HashMap();
        if(dto.getCurrentPage() == null || dto.getCurrentPage() < 1){
            dto.setCurrentPage(0);
        }
        if(dto.getPageSize() == null || dto.getPageSize() < 1 ){
            dto.setPageSize(15);
        }
        Map params = new HashMap();
        params.put("pageSize", dto.getPageSize());
        params.put("currentPage", dto.getCurrentPage());
        Long userId = null;
        Integer genre = null;
        if(!user.getUserRole().equals("SUPPER")){
            // 控制类型参数只能为0：避免前端随意传参
            if(user.getUserRole().equals("ADMIN")){ // 只有管理员才可筛选所有用户直播
                if(dto.getGenre() == null || dto.getGenre() != 0){
                    userId = user.getId();
                }else{
                    params.put("genre", genre);
                }
            }else{
                userId = user.getId();
            }
            params.put("userId", userId);
        }

        params.put("type", 0);
        if(dto.getOrderBy() == null){
            params.put("orderBy", "addTime");
        }
        if(dto.getOrderType() == null){
            params.put("orderType", "DESC");
        }
        params.put("deleteStatus", "deleteStatus");
        List<Video> videoList = this.videoService.findObjByMap(params);
        data.put("obj", videoList);
        SysConfig configs = this.configService.findSysConfigList();
        data.put("domain", configs.getDomain());
        data.put("currentPage", dto.getCurrentPage());
        data.put("pageSize", videoList.size());
        return new Result(200,"Successfully", data);
    }

    @RequiresPermissions("ADMIN:VIDEO:LIST")
    @ApiOperation("回放列表")
    @PostMapping("/playback/list")
    public Object playback(@RequestBody(required=false) VideoDto dto){
        User user = ShiroUserHolder.currentUser();
        Map data = new HashMap();
        if(dto.getCurrentPage() == null || dto.getCurrentPage() < 1){
            dto.setCurrentPage(1);
        }
        if(dto.getPageSize() == null || dto.getPageSize() < 1 ){
            dto.setPageSize(15);
        }
        Map params = new HashMap();
        params.put("pageSize", dto.getPageSize());
        params.put("currentPage", dto.getCurrentPage());
        Long userId = null;
        Integer genre = null;
        if(!user.getUserRole().equals("SUPPER")){
            // 控制类型参数只能为0：避免前端随意传参
            if(user.getUserRole().equals("ADMIN")){ // 只有管理员才可筛选所有用户直播
                if(dto.getGenre() == null || dto.getGenre() != 0){
                    userId = user.getId();
                }else{
                    params.put("genre", genre);
                }
            }else{
                userId = user.getId();
            }
            params.put("userId", userId);
        }
        params.put("type", 1);
        if(dto.getOrderBy() == null){
            params.put("orderBy", "addTime");
        }
        if(dto.getOrderType() == null){
            params.put("orderType", "DESC");
        }
        params.put("deleteStatus", "deleteStatus");
        List<Video> videos = this.videoService.findObjByMap(params);
        data.put("obj", videos);
        SysConfig configs = this.configService.findSysConfigList();
        data.put("domain", configs.getDomain());
        data.put("currentPage", dto.getCurrentPage());
        data.put("pageSize", videos.size());
        return new Result(200,"Successfully", data);
    }

  /*  @ApiOperation("视频保存")
    @PostMapping("/save")
    public Object save(){

    }*/

    /*@ApiOperation("录播文件上传接口")
    @PostMapping("/save")
    public Object upload(String id, @RequestParam(value = "file", required = false) MultipartFile file, String path){
      //   String path = "";// 视频保存路径

        if (file == null && file.getSize() <= 0) {
            return false;
        }
        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String picNewName = fileName + ext;
        String imgRealPath = path + File.separator + picNewName;

        java.io.File imageFile = new File(imgRealPath);
        try {
            file.transferTo(imageFile);
            Accessory accessory = new Accessory();
            accessory.setA_name("Video");
            accessory.setA_path(imgRealPath);
            accessory.setA_ext(ext);
            accessory.setA_size((int)file.getSize());
            accessory.setType(1);
            this.accessoryService.save(accessory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseUtil.result(200, "保存成功");
    }*/

    @RequiresPermissions("ADMIN:VIDEO:ADD")
    @ApiOperation("视频添加")
    @RequestMapping("/add")
    public Object add(){
        User user = ShiroUserHolder.currentUser();
        Map data = new HashMap();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("currentPage", 1);
        params.put("pageSize", 15);
        params.put("display", 1);
        List<Course> courseList = this.courseService.findBycondition(params);

        List<Grade> grades = this.gradeService.findBycondition(params);
        data.put("courseList", courseList);
        data.put("gradeList", grades);

        params.clear();
        params.put("startRow", 0);
        params.put("pageSize", 15);
        List<RoomProgram> roomProgramList = this.roomProgramService.getRoomProgram(params);
        data.put("roomProgramList", roomProgramList);

        params.clear();
        params.put("userId", user.getId());
        params.put("isEnable", 1);

        if(user.getUserRole().equals("SUPPER")){
            user = null;
        }
/*        params.clear();
        params.put("userId", user != null ? user.getId() : null);
        params.put("startRow", 0);
        params.put("pageSize", 15);
        List<LiveRoom> liveRooms = this.liveRoomService.query(params);
        data.put("liveRoomList", liveRooms);*/

        return ResponseUtil.ok(data);
    }

    @RequiresPermissions("ADMIN:VIDEO:UPDATE")
    @ApiOperation("视频修改")
    @PostMapping("/update")
    public Object update(@RequestBody VideoDto videoDto){
        User user = ShiroUserHolder.currentUser();
        Map data = new HashMap();
        SysConfig configs = this.configService.findSysConfigList();
        data.put("domain", configs.getDomain());
        Video video = this.videoService.selectPrimaryById(videoDto.getId());
        if(video != null){
            if(video.getGenre() == 1){
                RoomProgram roomProgram = this.roomProgramService.findObjById(video.getRoomProgramId());
                if(roomProgram != null){
                    video.setRoomProgram(roomProgram);
                }
            }else{
                LiveRoom liveRoom = this.liveRoomService.getObjById(video.getLiveRoomId());
                if(liveRoom != null){
                    video.setLiveRoom(liveRoom);
                }
            }
            data.put("obj", video);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("currentPage", 1);
            params.put("pageSize", 15);
            params.put("display", 1);
            List<Course> courseList = this.courseService.findBycondition(params);
            List<Grade> grades = this.gradeService.findBycondition(params);
            data.put("courseList", courseList);
            data.put("gradeList", grades);

            // 所有直播节目
            if(user.getUserRole().equals("SUPPER")){
                user = null;
            }
/*            params.clear();
            params.put("userId", user != null ? user.getId() : null);
            params.put("startRow", 0);
            params.put("pageSize", 15);
            List<LiveRoom> liveRooms = this.liveRoomService.query(params);
            data.put("liveRoomList", liveRooms);*/

        }
        return new Result(200, "Successfully", data);
    }

    @RequiresPermissions("ADMIN:VIDEO:SAVE")
    @ApiOperation("视频添加")
    @PostMapping("/save")
    public Object save(VideoDto videodto,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestParam(value = "img", required = false) MultipartFile img){
        SysConfig configs = this.configService.findSysConfigList();
        if(file != null && file.getSize() > 0){
            Accessory video = this.upload(file,1);
            videodto.setVideo(video);
        }
        if(img != null && img.getSize() > 0){
            Accessory photo = this.upload(img,0);
            videodto.setAccessory(photo);
        }
        Result result = (Result) this.videoService.save(videodto);
        if(result.getCode() != 200){
            return result;
        }
        return ResponseUtil.result(200, "保存成功");
    }



   /* @ApiOperation("录播修改")
    @PostMapping("/update")
    @RequiresPermissions("ADMIN_VIDEO_UPDATE")
    public Object update(VideoDto videodto,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestParam(value = "img", required = false) MultipartFile img){

        SysConfig configs = this.configService.findSysConfigList();
        if (file == null || file.getSize() <= 0 || img ==null && img.getSize() <= 0) {
            return new Result(500, "File does not exist");
        }
        Accessory video = this.upload(file,1);
        videodto.setVideo(video);
        Accessory photo = this.upload(img,0);
        videodto.setPhoto(photo);
        Result result = (Result) this.videoService.update(videodto);
        if(result.getCode() != 200){
            return result;
        }
        return ResponseUtil.result(200, "保存成功");
    }
*/

    public Accessory upload(@RequestParam(required = false) MultipartFile file, int type){
        SysConfig configs = this.configService.findSysConfigList();
        String uploaFilePath = configs.getUploadFilePath();
        String path = configs.getVideoFilePath();
        if(type == 0){
            path = configs.getPhotoFilePath();
            uploaFilePath = uploaFilePath + File.separator + "photo";
        }else{
            uploaFilePath = uploaFilePath + File.separator + "video";
        }
        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String picNewName = fileName + ext;
        String imgRealPath = path  + File.separator + picNewName;

        java.io.File imageFile = new File(imgRealPath);
        if (!imageFile.getParentFile().exists()) {
            imageFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(imageFile);
            Accessory accessory = new Accessory();
            accessory.setA_name(picNewName);
            accessory.setA_path(uploaFilePath);
            accessory.setA_ext(ext);
            accessory.setA_size((int)file.getSize());
            accessory.setType(type);
            this.accessoryService.save(accessory);
            return accessory;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresPermissions("ADMIN:VIDEO:DELETE")
    @ApiOperation("录播回放视频删除")
    @RequestMapping("/delete")
    public Object delete(@RequestBody VideoDto dto){
        boolean flag = this.videoService.delete(dto.getId());
        if(flag){
            return ResponseUtil.result(200, "Successfully");
        }
        return ResponseUtil.result(200, "Fail to delete");
    }

    @RequiresPermissions("ADMIN:VIDEO:AUDIT")
    @ApiOperation("视频审核")
    @PostMapping("/audit")
    public Object audit(@RequestBody VideoDto dto){
        if(dto != null){
            Video video = this.videoService.getObjById(dto.getId());
            User user = ShiroUserHolder.currentUser();

            if(video != null && video.getGenre() == 0){
                if(dto.getStatus() > 1 || dto.getStatus() < -1){
                    return ResponseUtil.badArgument("禁止恶意篡改参数");
                }
                dto.setLiveRoomId(video.getLiveRoomId());
                if( this.videoService.update(dto)){
                    return ResponseUtil.ok();
                }else{
                    return ResponseUtil.error();
                }
            }

            return ResponseUtil.badArgument("未找到指定资源");
        }
        return ResponseUtil.badArgument("未找到指定资源");
    }

    @RequiresPermissions("ADMIN:VIDEO:CHANGE")
    @ApiOperation("直播修改")
    @PutMapping("/change")
    public Object chenge(@RequestBody VideoReq req){
        if(req.getId() != null){
            Video video = this.videoService.getObjById(req.getId());
            int display = video.getDisplay()== 1 ? 0 : 1;
            video.setDisplay(display);
            this.videoService.update(video);
            return ResponseUtil.ok();
        }
        return ResponseUtil.badArgument();
    }

    @RequiresPermissions("ADMIN:VIDEO:APPLY")
    @ApiOperation("重新提交审核")
    @PutMapping("/apply")
    public Object apply(@RequestBody VideoReq req){
        if(req.getId() != null){
            Video video = this.videoService.getObjById(req.getId());
            video.setStatus(0);
            try {
                this.videoService.update(video);
                return ResponseUtil.ok();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseUtil.error();
            }
        }
        return ResponseUtil.badArgument();
    }

    @RequiresPermissions("ADMIN:VIDEO:WAITREVIW")
    @ApiOperation("待审核视频")
    @PostMapping("/waitReview")
    public Object waitReview(@RequestBody(required = false) VideoDto dto){
        dto.setGenre(0);
        Page<Video> page = this.videoService.query(dto);
        if(page.getResult().size() > 0){
            Map data = new HashMap();
            data.put("obj", new PageInfo<RoomProgram>(page));
            SysConfig config = this.sysConfigService.findSysConfigList();
            data.put("domain", config.getDomain());
            return  ResponseUtil.ok(data);
        }
        return ResponseUtil.ok();
    }

    @ApiOperation("视频管理-列表")
    @RequestMapping("/manager/list")
    public Object managerList(@RequestBody(required = false) VideoDto dto){
        if(dto == null){
            dto = new VideoDto();
        }
        Page<Video> page = this.videoService.query(dto);
        if(page.getResult().size() > 0){
            Map map = new HashMap();
            map.put("obj", new PageInfo(page));
            SysConfig configs = this.configService.findSysConfigList();
            map.put("domain", configs.getDomain());
            return ResponseUtil.ok(map);
        }
        return ResponseUtil.ok();
    }

}
