package com.cloud.tv.core.manager.admin.action;

import com.cloud.tv.dto.PermissionDto;
import com.cloud.tv.entity.Res;
import com.cloud.tv.entity.Role;
import com.github.pagehelper.Page;
import com.cloud.tv.core.service.IResService;
import com.cloud.tv.core.service.IRoleService;
import com.cloud.tv.core.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("权限管理")
@RestController
@RequestMapping("/admin/permission")
public class PermissionManagerController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IResService resService;

    @RequiresPermissions("ADMIN:PERMISSION:LIST")
    @ApiOperation("权限列表")
    @PostMapping("/list")
    public Object list(@RequestBody PermissionDto dto){
        Map data = new HashMap();
        if(dto.getCurrentPage() == null || dto.getCurrentPage().equals("")){
            dto.setCurrentPage(1);
        }
        if(dto.getPageSize() == null || dto.getPageSize().equals("")){
            dto.setPageSize(15);
        }
        Map params = new HashMap();
        params.put("currentPage", (dto.getCurrentPage()));
        params.put("pageSize", dto.getPageSize());
        Page<Res> page = this.resService.query(params);
        data.put("obj", page.getResult());
        data.put("total", page.getTotal());
        data.put("currentPage", page.getPageNum());
        data.put("pageSize", page.getPageSize());
        data.put("pages", page.getPages());

        return ResponseUtil.ok(data);
    }
    /*public Object list(@RequestBody PermissionDto dto){
        if(dto.getCurrentPage() == null || dto.getCurrentPage().equals("")){
            dto.setCurrentPage(1);
        }
        if(dto.getPageSize() == null || dto.getPageSize().equals("")){
            dto.setPageSize(15);
        }
        Map params = new HashMap();
        params.put("startRow", (dto.getCurrentPage()) * dto.getPageSize());
        params.put("pageSize", dto.getPageSize());
        return ResponseUtil.ok(this.resService.query(params));
    }*/

    @RequiresPermissions("ADMIN:PERMISSION:ADD")
    @ApiOperation("权限添加")
    @PostMapping("/add")
    public Object add(){
        List<Role> roles = this.roleService.findRoleByType("ADMIN");
        return ResponseUtil.ok(roles);
    }

    @RequiresPermissions("ADMIN:PERMISSION:UPDATE")
    @ApiOperation("权限更新")
    @PostMapping("/update")
    public Object update(@RequestBody PermissionDto dto){
        Map data = new HashMap();
        Res res = this.resService.findResUnitRoleByResId(dto.getId());
        if(res != null){
            data.put("obj", res);

            List<Role> roles = this.roleService.findRoleByType("ADMIN");
            data.put("roles", roles);
            return ResponseUtil.ok(data);
        }
        return ResponseUtil.badArgument();
    }

    public Object verify(PermissionDto dto){
        String name = dto.getName();
        if(name == null || name.equals("")){
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("ADMIN:PERMISSION:SAVE")
    @ApiOperation("权限保存")
    @PostMapping("/save")
    public Object save(@RequestBody PermissionDto dto){
        if(dto != null){
            Object error = this.verify(dto);
            if(error != null){
                return error;
            }
            if(this.resService.save(dto)){
                return ResponseUtil.ok();
            }
            return ResponseUtil.add();
        }
        return ResponseUtil.badArgument();
    }

    @RequiresPermissions("ADMIN:PERMISSION:DELETE")
    @ApiOperation("权限删除")
    @DeleteMapping("/delete")
    public Object delete(Long id){
        Res res = this.resService.findObjById(id);
        if(res != null){
            if(this.resService.delete(res.getId())){
                return ResponseUtil.ok();
            }
            return ResponseUtil.error();
        }
        return ResponseUtil.badArgument();
    }

}
