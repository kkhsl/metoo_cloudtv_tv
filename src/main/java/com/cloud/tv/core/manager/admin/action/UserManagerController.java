package com.cloud.tv.core.manager.admin.action;

import com.cloud.tv.core.manager.admin.tools.ShiroUserHolder;
import com.cloud.tv.core.service.*;
import com.cloud.tv.core.utils.CommUtils;
import com.cloud.tv.core.utils.ResponseUtil;
import com.cloud.tv.dto.UserDto;
import com.cloud.tv.entity.*;
import com.cloud.tv.vo.UserVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.util.StringUtil;
import com.cloud.tv.core.shiro.tools.SaltUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("用户管理")
@RestController
@RequestMapping("/admin/user")
public class UserManagerController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRoleGroupService roleGroupService;
    @Autowired
    private ILiveRoomService liveRoomService;
    @Autowired
    private IRoomProgramService roomProgramService;
    @Autowired
    private IVideoService videoService;

    @RequiresPermissions("ADMIN:USER:LIST")
    @ApiOperation("用户列表")
    @PostMapping("/list")
    public Object list(@RequestBody UserDto dto){
        Map map = new HashMap();
        if(dto.getCurrentPage() == null || dto.getCurrentPage().equals("")){
            dto.setCurrentPage(1);
        }
        if(dto.getPageSize() == null || dto.getPageSize().equals("")){
            dto.setPageSize(15);
        }
        Map params = new HashMap();
        params.put("deleteStatus", 0);
        params.put("pageSize", dto.getPageSize());
        params.put("pageSize", dto.getPageSize());
        params.put("currentPage", (dto.getCurrentPage() ));
        Page<UserVo> page = this.userService.query(params);
        map.put("currentPage", page.getStartRow());
        map.put("pageSize", page.getPageSize());
        map.put("obj", page.getResult());
        map.put("pages", page.getPages());
        return ResponseUtil.ok(map);
    }

    @RequiresPermissions("ADMIN:USER:ADD")
    @ApiOperation("用户添加")
    @GetMapping("/add")
    public Object add(){
        // 查询角色组列表/角色列表
        Map params = new HashMap();
        params.put("currentPage", 1);
        params.put("pageSize", 15);
        List<RoleGroup> roleGroupList = this.roleGroupService.roleUnitGroup(params);
        Map map  = new HashMap();
        map.put("currentPage", 1);
        map.put("pageSize", 15);
        map.put("roleGroup", roleGroupList);
        return ResponseUtil.ok(map);
    }

    @RequiresPermissions("ADMIN:USER:UPDATE")
    @ApiOperation("用户更新")
    @PostMapping("/update")
    public Object update(@RequestBody UserDto dto){
        User user = this.userService.findObjById(dto.getId());
        if(user != null){
            Map map = new HashMap();

            // 根据用户ID查询角色
          /*  List<Role> roleList = this.roleService.findRoleByUserId(user.getId());
            map.put("roleList",roleList);*/
            Map params = new HashMap();
            params.put("currentPage", 1);
            params.put("pageSize", 15);
            List<RoleGroup> roleGroupList = this.roleGroupService.roleUnitGroup(params);
            map.put("roleGroup", roleGroupList);
            UserVo obj = this.userService.findUserUpdate(user.getId());
            map.put("obj", obj);
            return ResponseUtil.ok(map);
        }
        return ResponseUtil.badArgument();
    }

    @RequiresPermissions("ADMIN:USER:SAVE")
    @ApiOperation("用户保存")
    @PostMapping("/save")
    public Object save(@RequestBody UserDto dto){
        if(dto != null && dto.getId() != null){
            if(dto.getUsername() != null && !dto.getUsername().equals("")){
                User currentUser = ShiroUserHolder.currentUser();
                User user = this.userService.findByUserName(dto.getUsername());
                if(user != null){
                    if(!currentUser.getUsername().equals(dto.getUsername())){// 修改用户名强制退出已登录用户
                        dto.setFlag(true);
                    }
                    if(currentUser.getUsername().equals(dto.getUsername())
                            && currentUser.getSex().equals(dto.getSex())
                            && currentUser.getAge().equals(dto.getAge())
                            && StringUtils.isEmpty(dto.getPassword())
                            && StringUtils.isEmpty(dto.getVerifyPassword())){
                        return ResponseUtil.ok();
                    }
                    if(!user.getId().equals(currentUser.getId())){
                        return ResponseUtil.fail(400, "用户已存在");
                    }
                }else{
                    dto.setFlag(true);
                }
                 if(!StringUtils.isEmpty(dto.getPassword()) || !StringUtils.isEmpty(dto.getVerifyPassword())){
                    String oldPassword = CommUtils.password(dto.getOldPassword(), currentUser.getSalt());
                    if(!currentUser.getPassword().equals(oldPassword)){
                        return ResponseUtil.badArgument("旧密码和原始密码不一致");
                    }
                    String newPassword = CommUtils.password(dto.getPassword(), currentUser.getSalt());
                    if(newPassword.equals(oldPassword)){
                        return ResponseUtil.badArgument("新密码不能和旧密码相同");
                    }
                    if(!dto.getPassword().equals(dto.getVerifyPassword())){
                        return ResponseUtil.badArgument("新密码和确认密码不一致");
                    }
                    if(dto.getPassword().length() < 6 || dto.getPassword().length() > 20){
                         return ResponseUtil.badArgument("设置6-20位新密码");
                    }
                    String sale = SaltUtils.getSalt(8);
                    dto.setPassword(CommUtils.password(dto.getPassword(), sale));
                    dto.setSalt(sale);
                    dto.setFlag(true);
                }
                if( this.userService.save(dto)){
                    return ResponseUtil.ok();
                }
                return ResponseUtil.error();
            }
                return ResponseUtil.badArgument("请输入用户名");
        }
        return ResponseUtil.badArgument("请输入用户ID");
    }

//    @RequiresPermissions("ADMIN:USER:SAVE")
    @ApiOperation("创建用户")
    @PostMapping("/create")
    public Object persionalSave(@RequestBody UserDto dto){
        if(dto != null){
            if(StringUtil.isEmpty(dto.getUsername())){// 新增时必须验证密码
                return ResponseUtil.badArgument("请输入用户名");
            }
            if(dto.getId() == null && StringUtils.isEmpty(dto.getPassword())){
                return ResponseUtil.badArgument("请输入密码");
            }
            User currentUser = this.userService.findObjById(dto.getId());
            User user = this.userService.findByUserName(dto.getUsername());
            if(user != null){
                if(currentUser != null){
                    if(!user.getId().equals(currentUser.getId())){
                        return ResponseUtil.fail(400, "用户已存在");
                    }
                }else{
                    return ResponseUtil.fail(400, "用户已存在");
                }
            }

            if(!StringUtils.isEmpty(dto.getPassword())){
                if(dto.getPassword().length() < 6 || dto.getPassword().length() > 20){
                    return ResponseUtil.badArgument("设置6-20位新密码");
                }else{
                    String sale = SaltUtils.getSalt(8);
                    String password = CommUtils.password(dto.getPassword(), sale);
                    dto.setPassword(password);
                    dto.setSalt(sale);
                }
            }
            if(StringUtils.isEmpty(dto.getType())){
                return ResponseUtil.badArgument("请选择用户类型");
            }
            if( this.userService.save(dto)){
                return ResponseUtil.ok();
            }
            return ResponseUtil.error();
         }
        return ResponseUtil.badArgument();
     }


    @RequiresPermissions("ADMIN:USER:DELETE")
    @ApiOperation("用户删除")
    @RequestMapping("/delete")
    public Object delete(@RequestBody UserDto dto){
        User user = this.userService.findObjById(dto.getId());
        if(user != null){
            user.setDeleteStatus(-1);
            this.userService.update(user);
            // 清空用户直播间
            Map params = new HashMap();
            params.put("userId", user.getId());
            List<LiveRoom> liveRoomList = this.liveRoomService.findObjByMap(params);
            for(LiveRoom liveRoom : liveRoomList){
                liveRoom.setDeleteStatus(-1);
                this.liveRoomService.update(liveRoom);
            }
            // 清空用户直播
            List<RoomProgram> roomProgramList = this.roomProgramService.findObjByCondition(params);
            for(RoomProgram roomProgram : roomProgramList){
                roomProgram.setDeleteStatus(-1);
                this.roomProgramService.update(roomProgram);
            }
            // 清空用户视频
            params.put("pageSize", 0);
            params.put("currentPage", 0);
           List<Video> videoList = this.videoService.findObjByMap(params);
            for(Video video : videoList){
                video.setDeleteStatus(-1);
                this.videoService.update(video);
            }
            /*if(  this.userService.delete(user)){
                return ResponseUtil.ok();
            }*/
            return ResponseUtil.ok();
        }
        return ResponseUtil.badArgument();
    }

    @ApiOperation("个人中心")
    @RequestMapping("/personal")
    public Object personal(){
        User user = ShiroUserHolder.currentUser();
        if(user == null){
            return ResponseUtil.unlogin();
        }
        return user;
    }

}
