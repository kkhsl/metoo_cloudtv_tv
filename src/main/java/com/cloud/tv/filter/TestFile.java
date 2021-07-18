package com.cloud.tv.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//@Order(2)
//@WebFilter("/jwt/*")
public class TestFile implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("1111");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("22225");
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {
        System.out.println("33335");
    }
}
