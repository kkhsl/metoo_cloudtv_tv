package com.cloud.tv.core.manager.admin.action;

import com.cloud.tv.core.service.IResService;
import com.cloud.tv.core.service.IRoleGroupService;
import com.cloud.tv.core.service.IRoleService;
import com.cloud.tv.core.utils.ResponseUtil;
import com.cloud.tv.dto.RoleDto;
import com.cloud.tv.entity.Res;
import com.cloud.tv.entity.Role;
import com.cloud.tv.entity.RoleGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("角色管理")
@RestController
@RequestMapping("/admin/role")
public class RoleManagerController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRoleGroupService roleGroupService;
    @Autowired
    private IResService resService;

//    @RequiresPermissions("ADMIN:ROLE:LIST")
    @ApiOperation("角色列表")
    @RequestMapping("/list")
    public Object list(@RequestBody RoleDto dto){
        Map params = new HashMap();
        /*if(dto.getCurrentPage() == null || dto.getCurrentPage() < 1){
            dto.setCurrentPage(1);
        }
        if(dto.getPageSize() == null || dto.getPageSize() < 1 ){
            dto.setPageSize(15);
        }*/
        params.put("startRow", (dto.getCurrentPage() ));
        params.put("pageSize", dto.getPageSize());
        params.put("type", "ADMIN");
        List<Role> roles = this.roleService.query(params);
        params.put("obj", roles);
        params.put("pageSize", roles.size());
        return  ResponseUtil.ok(params);
    }

    @RequiresPermissions("ADMIN:ROLE:ADD")
    @ApiOperation("角色添加")
    @GetMapping("/add")
    public Object add(){
        Map map = new HashMap();
        List<RoleGroup> roleGroupList = this.roleGroupService.selectByPrimaryType("ADMIN");
        map.put("roleGroupList", roleGroupList);
        Map params = new HashMap();
        params.put("currentPage", 1);
        params.put("pageSize", 1000);
        List<Role> resList = this.resService.query(params);
        map.put("resList", resList);
        return ResponseUtil.ok(map);
    }

   /* @ApiOperation("角色添加")
    @GetMapping("/add")
    public Object add(){
        Map map = new HashMap();
        // 查询角色组
        List<RoleGroup> roleGroupList = this.roleGroupService.selectByPrimaryType("ADMIN");
        map.put("roleGroup", roleGroupList);
        return ResponseUtil.ok();
    }*/

    private Object validate(RoleDto role) {
        String name = role.getName();
        String code = role.getRoleCode();
        if (code == null || name == null) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("ADMIN:ROLE:UPDATE")
    @ApiOperation("角色修改/回显")
    @PostMapping("/update")
    public Object udpate(@RequestBody RoleDto dto){
        Role role = this.roleService.findRoleById(dto.getId());
        if(role != null){
            Map map =  new HashMap();
            map.put("obj", this.roleService.selectByPrimaryUpdae(dto.getId()));
            map.put("roleGroupList", this.roleGroupService.selectByPrimaryType("ADMIN"));
            map.put("roleResList", this.resService.findResByRoleId(role.getId()));
            Map params = new HashMap();
            params.put("currentPage", 1);
            params.put("pageSize", 1000);
            List<Res> resList = this.resService.query(params);
            map.put("resList", resList);
            return ResponseUtil.ok(map);
        }
        return ResponseUtil.badArgument();
    }

    @RequiresPermissions("ADMIN:ROLE:SAVE")
    @ApiOperation("角色保存")
    @PostMapping("/save")
    public Object save(@RequestBody RoleDto roleDto){
        Object error = validate(roleDto);
        if(error != null){
            return error;
        }
        if(roleDto.getId() == null){
            if(this.roleService.countBy(roleDto.getRoleCode())){
                return ResponseUtil.fail(400, "Role is exists");
            }
        }
        if(this.roleService.save(roleDto)){
            return ResponseUtil.ok();
        }
        return ResponseUtil.error();
    }

    /*@ApiOperation("角色删除")
    @PostMapping("/delete")
    public Object delete(Long id){
        Role role = this.roleService.findRoleById(id);
        if(role != null){
            RoleDto roleDto = new RoleDto();
            role.setRoleGroup(null);
            BeanUtils.copyProperties(role, roleDto);
            this.roleService.save(roleDto);
            if(this.roleService.delete(role.getId())){
                return ResponseUtil.ok();
            }
            return ResponseUtil.delete();
        }
        return ResponseUtil.badArgument();
    }*/

    @RequiresPermissions("ADMIN:ROLE:DELETE")
    @ApiOperation("角色删除")
    @RequestMapping("/delete")
    public Object delete(@RequestBody RoleDto dto){
        Role role = this.roleService.findRoleById(dto.getId());
        if(role != null){
            // 根据角色ID查询权限
            List<Res> resList =  this.resService.findResByRoleId(role.getId());
            if(resList.size() > 0){
                return ResponseUtil.fail("已有用户关联当前角色，禁止删除");
            }
            if(this.roleService.delete(role.getId())){
                return ResponseUtil.ok();
            }
        }

        return ResponseUtil.delete();
    }
}
