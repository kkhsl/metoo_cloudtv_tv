package com.hkk.cloudtv.config.shiro;

import com.hkk.cloudtv.core.service.IRegisterService;
import com.hkk.cloudtv.core.service.IResService;
import com.hkk.cloudtv.core.service.IRoleService;
import com.hkk.cloudtv.core.shiro.salt.MyByteSource;
import com.hkk.cloudtv.core.shiro.tools.ApplicationContextUtils;
import com.hkk.cloudtv.entity.Res;
import com.hkk.cloudtv.entity.Role;
import com.hkk.cloudtv.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 *     Title: MyRealm.java
 * </p>
 *
 * <p>
 *     Description: 自定义Realm
 * </p>
 *
 *         for(Role role : roles){
 *                     System.out.println("角色：" + role.getType());
 *                     simpleAuthorizationInfo.addRole(role.getType());
 *                     simpleAuthorizationInfo.addStringPermission("BUYER:*:*");
 *                 }
 * <p>
 *     authen: hkk
 * </p>
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IResService resService;
    @Autowired
    private IRegisterService registerService;

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取身份信息
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        // 根据身份信息获取角色信息，资源信息

       //User user = registerService.findByUsername(primaryPrincipal);
        User user = registerService.findRolesByUserName(primaryPrincipal);
        List<Role> roles = this.roleService.findRoleByUserId(user.getId());//user.getRoles();
        if(!CollectionUtils.isEmpty(roles)){
            if(user != null){
                String username = user.getUsername();
                SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

               /* roles.forEach(role->{
                     simpleAuthorizationInfo.addRole(role.getRoleCode());
                   // simpleAuthorizationInfo.addRole(role.getType());
                    //System.out.println(role.getName());
                    //simpleAuthorizationInfo.addStringPermission("BUYER:*:*");
                   // simpleAuthorizationInfo.addStringPermission(role.getType() + ":*:*");
                   // simpleAuthorizationInfo.addStringPermission(role.getType() + ":*");
                    List<Res> reses = resService.findResByRoleId(role.getId());
                    if(!CollectionUtils.isEmpty(reses)){
                        reses.forEach(res -> {
                            simpleAuthorizationInfo.addStringPermission(res.getValue());
                        });
                    }
                });*/

                for(Role role : roles){
                    simpleAuthorizationInfo.addRole(role.getRoleCode());
                    System.out.println("roleCode_test : "  + role.getRoleCode());
                    // simpleAuthorizationInfo.addRole(role.getType());
                    //System.out.println(role.getName());
                    //simpleAuthorizationInfo.addStringPermission("BUYER:*:*");
                    // simpleAuthorizationInfo.addStringPermission(role.getType() + ":*:*");
                    // simpleAuthorizationInfo.addStringPermission(role.getType() + ":*");
                    List<Res> reses = resService.findResByRoleId(role.getId());
                    if(!CollectionUtils.isEmpty(reses)){
                        reses.forEach(res -> {
                            System.out.println("test_value: " + res.getValue());
                            simpleAuthorizationInfo.addStringPermission(res.getValue());
                        });
                    }
                }
           /* if(primaryPrincipal.equals(username)){
                SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
                // simpleAuthorizationInfo.setRoles("buyer");
                if(user.getUserRole().equals("BUYER")){
                    simpleAuthorizationInfo.addRole("BUYER");
                    simpleAuthorizationInfo.addStringPermission("BUYER:*:*");
                }else if(user.getUserRole().equals("SELLER")){
                    simpleAuthorizationInfo.addRole("SELLER");
                    simpleAuthorizationInfo.addStringPermission("SELLER:*:*");
                }else if(user.getUserRole().equals("ADMIN")){
                    simpleAuthorizationInfo.addRole("ADMIN");
                    simpleAuthorizationInfo.addStringPermission("ADMIN:*:*");
                }*/
                return simpleAuthorizationInfo;
            }
        }
        return null;
    }
    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        // 模拟数据
      /*  if("zhangsan".equals(username)){
            return new SimpleAuthenticationInfo("zhangsan","123456",this.getName());
        }
*/
        // 真实数据
        IRegisterService registerService = (IRegisterService) ApplicationContextUtils.getBean("registerService");

        User user = registerService.findByUsername(username);
        if(!ObjectUtils.isEmpty(user)){
            if(username.equals(user.getUsername())){
                return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),  new MyByteSource(user.getSalt()), this.getName());
            }
        }

        return null;
    }
}
