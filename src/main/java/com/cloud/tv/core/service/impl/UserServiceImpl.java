package com.cloud.tv.core.service.impl;

import com.cloud.tv.core.manager.admin.tools.ShiroUserHolder;
import com.cloud.tv.core.mapper.BuyerMapper;
import com.cloud.tv.core.mapper.LiveRoomMapper;
import com.cloud.tv.core.service.*;
import com.cloud.tv.core.utils.CommUtils;
import com.cloud.tv.core.utils.FileUtil;
import com.cloud.tv.dto.UserDto;
import com.cloud.tv.entity.*;
import com.cloud.tv.vo.UserVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cloud.tv.core.service.*;
import com.cloud.tv.entity.*;
import com.cloud.tv.core.service.*;
import com.cloud.tv.entity.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private BuyerMapper buyerMapper;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ILiveRoomService liveRoomService;
    @Autowired
    private LiveRoomMapper liveRoomMapper;

    @Override
    public User findByUserName(String username) {
        return this.buyerMapper.findByUserName(username);
    }

    @Override
    public User findRolesByUserName(String username) {
        return null;
    }

    @Override
    public UserVo findUserUpdate(Long id) {
        return this.buyerMapper.findUserUpdate(id);
    }

    @Override
    public User findObjById(Long id) {
        return this.buyerMapper.selectPrimaryKey(id);
    }

    @Override
    public Page<UserVo> query(UserDto dto) {
        Page<UserVo> page = PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        this.buyerMapper.query(dto);
        return page;
    }


    @Override
    public boolean save(UserDto dto) {
        User user = null;
        if(dto.getId() == null){
            user = new User();
            dto.setAddTime(new Date());
        }else{
            user = this.buyerMapper.selectPrimaryKey(dto.getId());
        }
        BeanUtils.copyProperties(dto, user);
        if(dto.getId() == null){
            try {
                this.buyerMapper.insert(user);
                // 创建直播间
                Map params = new HashMap();
                params.put("currentPage", 0);
                params.put("pageSize", 1);
                params.put("userId", user.getId());
                List<LiveRoom> liveRoomList = this.liveRoomService.findObjByMap(params);
               if(liveRoomList.size() < 1){
                   LiveRoom instance = new LiveRoom();
                   instance.setDeleteStatus(0);
                   instance.setAddTime(new Date());
                   instance.setTitle(user.getUsername() + "的直播间");
                   Date date = new Date();
                   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                   String df = sdf.format(date);
                   String bindCode = df + CommUtils.randomString(6);// 推流码
                   instance.setBindCode(bindCode);
                   instance.setUser(user);
                   instance.setUserId(user.getId());
                   instance.setUsername(user.getUsername());
                   instance.setIsEnable(1);
                   if(instance.getManager() == null || instance.getManager().equals("")){
                       instance.setManager(user.getUsername());
                   }
                   //rtmp://lk.soarmall.com:1935/hls
                   SysConfig SysConfig = this.sysConfigService.findSysConfigList();
                   String rtmp = CommUtils.getRtmp(SysConfig.getIp(), bindCode);
                   String obsRtmp = CommUtils.getObsRtmp(SysConfig.getIp());
                   String path =  SysConfig.getPath() + File.separator + bindCode;
                   instance.setRtmp(rtmp);
                   instance.setObsRtmp(obsRtmp);
                   try {
                       FileUtil.storeFile(path);
                       FileUtil.possessor(path);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   this.liveRoomMapper.save(instance);
               }
                String roleName = "";
                // 批量添加用户角色信息
                if(dto.getRole_id() != null && dto.getRole_id().length > 0){
                    List<Integer> idList = Arrays.asList(dto.getRole_id());
                    List<Role> roleList = this.roleService.findRoleByIdList(idList);
                    List<UserRole> userRoles = new ArrayList<UserRole>();
                    for(Role role : roleList){
                        UserRole userRole = new UserRole();
                        userRole.setUser_id(user.getId());
                        userRole.setRole_id(role.getId());
                        userRoles.add(userRole);
                        roleName += role.getName() + ",";
                    }
                    roleName = roleName.substring(0,roleName.lastIndexOf(","));
                    this.userRoleService.batchAddUserRole(userRoles);
                }
                    try {
                        user.setUserRole(roleName);
                        this.buyerMapper.update(user);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else{
            try {
                String roleName = "";
                // 清除用户角色信息
                this.userRoleService.deleteUserByRoleId(user.getId());
                // 批量添加用户角色信息
                if(dto.getRole_id() != null && dto.getRole_id().length > 0){
                    List<Integer> idList = Arrays.asList(dto.getRole_id());
                    List<Role> roleList = this.roleService.findRoleByIdList(idList);
                    List<UserRole> userRoles = new ArrayList<UserRole>();
                    for(Role role : roleList){
                        UserRole userRole = new UserRole();
                        userRole.setUser_id(user.getId());
                        userRole.setRole_id(role.getId());
                        userRoles.add(userRole);
                        roleName += role.getName() + ",";
                    }
                    roleName = roleName.substring(0, roleName.lastIndexOf(","));
                    this.userRoleService.batchAddUserRole(userRoles);
                }
                user.setUserRole(roleName);
                this.buyerMapper.update(user);
                User currentUser = ShiroUserHolder.currentUser();


                // 第一种方式 强制退出当前帐号
                // 如果修改的是当前已登录用户信息则退出当前帐号
                if(dto.isFlag() && currentUser.getId().equals(user.getId())){
                     SecurityUtils.getSubject().logout();
                    // 修改身份信息后，动态更改Subject的用户属性
                    Subject subject = SecurityUtils.getSubject();
                    PrincipalCollection principalCollection = subject.getPrincipals();
                    String realmName = principalCollection.getRealmNames().iterator().next();
                    User userInfo = this.buyerMapper.findByUserName(user.getUsername());// 查询指定属性，封装到Subject内
                    PrincipalCollection newPrincipalCollection =
                            new SimplePrincipalCollection(userInfo, realmName);
                    subject.runAs(newPrincipalCollection);
                }

                //第二种防止 强制退出被修改用户

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public boolean update(User user) {
        try {
            this.buyerMapper.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(User user) {
        // 清除用户角色
        try {
            this.userRoleService.deleteUserByRoleId(user.getId());
            this.buyerMapper.delete(user.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
