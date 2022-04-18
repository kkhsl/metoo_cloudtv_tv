package com.cloud.tv.core.config.filter;

import com.alibaba.fastjson.JSONObject;
import com.cloud.tv.core.service.IUserService;
import com.cloud.tv.core.shiro.tools.ApplicationContextUtils;
import com.cloud.tv.entity.User;
import com.cloud.tv.vo.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyAccessControlFilter extends AccessControlFilter {


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        //判断用户是通过记住我功能自动登录,此时session失效
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.isAuthenticated());
        System.out.println(subject.isRemembered());
       // 如果未认证并且未IsreMenmberMe(Session失效问题)
        if(!subject.isAuthenticated()){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSONObject.toJSONString(new Result(401,"认证失败")));
            //自动登录失败,跳转到登录页面
//                response.sendRedirect(request.getContextPath()+"/login");
            return false;
        }
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }
}
