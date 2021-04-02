package com.hkk.cloudtv.config.shiro;

import com.hkk.cloudtv.core.shiro.cache.RedisCacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Title: ShiroConfig.java
 * </p>
 *
 * <p>
 * Description: 整合Shiro框架相关的配置类; Web环境中，自动为SecurityUtil注入Securitymanagers
 * </p>
 *
 * <p>
 * authen: hkk
 * </p>
 */
@Configuration
public class ShiroConfig {

    // 创建ShiroFilter  //负责拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 1,给过滤器设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        // 2,配置系统受限资源
        Map<String, String> res = new HashMap<String, String>();
     /* res.put("/index.jsp", "authc");// authc 请求这个资源需要认证和授权;参数可以为视图可以为路径（/index.jsp、/**、/path/*）
        res.put("/user/login", "anon");// 设置所有资源都受限；避免登录资源受限，设置登录为公共资源
        res.put("/user/register", "anon");*/
        res.put("/buyer/captcha", "anon");
        res.put("/buyer/login", "anon");// 设置所有资源都受限；避免登录资源受限，设置登录为公共资源
        res.put("/buyer/register", "anon");

        res.put("/admin/auth/401", "anon");
        res.put("/admin/auth/403", "anon");


        //res.put("/buyer/**", "authc");
        res.put("/admin/**", "authc");
        //res.put("/**", "authc");


        // 放行静态资源
        res.put("/static/**", "anon");
        res.put("/css/**", "anon");
        res.put("/js/**", "anon");
        res.put("/upload/**", "anon");

        //shiroFilterFactoryBean.setLoginUrl("/login.jsp");
        //shiroFilterFactoryBean.setLoginUrl("/buyer/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(res);
        // sshiroFilterFactoryBean.setLoginUrl("/user/login"); // 导致不断重定向

        shiroFilterFactoryBean.setLoginUrl("/admin/auth/401");
        // shiroFilterFactoryBean.setSuccessUrl("/admin/auth/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/admin/auth/403");

        // 3，配置系统公共资源
        return shiroFilterFactoryBean;

    }

    // 创建安全管理器 web环境中配置webSecurity
    @Bean
    public DefaultWebSecurityManager getDefaultWevSecurityManager(Realm realm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        // 1, 给安全管理器设置Realm
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;

    }

    // 创建自定义realm
    @Bean
    public Realm getRealm() {
        MyRealm myRealm = new MyRealm();
        // 设置Realm使用hash凭证匹配器; 问：Realm 不设置hash凭证器会出现什么
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);

        myRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        // 开启缓存管理
        //myRealm.setCacheManager(new EhCacheManager());// EhCache
       // myRealm.setCacheManager(new RedisCacheManager());// RedisCacheManager
        myRealm.setCachingEnabled(false);// 开启全局缓存

        /* 允许认证缓存 */
        myRealm.setAuthenticationCachingEnabled(false);
        myRealm.setAuthenticationCacheName("authenticationCache");
        /* 允许授权缓存 */
        myRealm.setAuthorizationCachingEnabled(false);
        myRealm.setAuthorizationCacheName("authorizationCache");

        return myRealm;
    }

}
