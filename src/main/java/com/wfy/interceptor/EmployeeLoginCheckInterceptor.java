package com.wfy.interceptor;

import com.alibaba.fastjson2.JSON;
import com.wfy.common.R;
import com.wfy.domain.Employee;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台员工登录检查拦截器
 */
public class EmployeeLoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 获取 Session域 的用户信息
        Long userInfo = (Long)request.getSession().getAttribute("userInfo");

        //2. 获取成功，放行
        if (userInfo != null) {
            return true;
        }

        //3. 获取失败，未登录，跳转至登录页面
//        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        response.sendRedirect("/backend/page/login/login.html");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
