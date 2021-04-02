package com.hkk.cloudtv.core.manager.admin.action;

import com.hkk.cloudtv.core.service.IRoleService;
import com.hkk.cloudtv.entity.Role;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class RoleManagerAction {

    @Autowired
    private IRoleService roleService;

/*    @RequiresPermissions("BUYER_PUT")
    @RequestMapping("/roles")
    public Object getAllRoles(HttpServletRequest request, HttpServletResponse response, String id){
        List<Role> roles = this.roleService.findRoleById(Long.valueOf(id));
        roles.forEach(role -> {
            System.out.println("Test_role_name:" + role.getName());
        });
        return roles;
    }*/

    @RequiresPermissions("BUYER_PUT")
    @RequestMapping("/roles")
    public Object getAllRoles(HttpServletRequest request, HttpServletResponse response, String type){
        List<Role> roles = this.roleService.findRoleByType(type);
        roles.forEach(role -> {
            System.out.println("Test_role_name:" + role.getName());
        });
        return roles;
    }
}
