package com.cloud.tv.core.manager.admin.action;

import com.cloud.tv.core.manager.admin.tools.ShiroUserHolder;
import com.cloud.tv.core.service.IIndexService;
import com.cloud.tv.core.utils.ResponseUtil;
import com.cloud.tv.entity.User;
import com.cloud.tv.vo.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("系统首页")
@RestController
@RequestMapping("/admin/index")
public class IndexManageController {

    @Autowired
    private IIndexService indexService;

//    @RequiresPerions("ADMIN:INDEX:MENU")
    @ApiOperation("系统菜单")
    @RequestMapping("/menu")
    public Object menu(){
        Map map = new HashMap();
        User user = ShiroUserHolder.currentUser();
        List<MenuVo> menuList = this.indexService.findMenu(user.getId());
        map.put("obj", menuList);
        map.put("userRole", user.getUserRole());
        return ResponseUtil.ok(map);
    }
}
