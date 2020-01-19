package com.youkeshu.hr.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 19:10
 * @Description:
 */
public class SessionInterceptor implements HandlerInterceptor {

    public static final String USER_AUTH = "user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //首页路径以及登录放行
        if ("/api/login".equals(request.getRequestURI()) || "/login".equals(request.getRequestURI())) {
            return true;
        }
        //重定向
        Object object = request.getSession().getAttribute("user");
        if (null == object) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}