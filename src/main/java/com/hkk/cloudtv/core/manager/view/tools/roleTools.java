package com.hkk.cloudtv.core.manager.view.tools;

import com.hkk.cloudtv.core.service.IRoleService;
import com.hkk.cloudtv.entity.Role;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class roleTools {


    private IRoleService roleService;

    public List<Role> getAllRole(String type){
        List<Role> roles = this.roleService.findRoleByType(type);

        return roles;
    }
}
