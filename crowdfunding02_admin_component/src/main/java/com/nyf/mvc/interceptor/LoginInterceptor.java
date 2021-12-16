package com.nyf.mvc.interceptor;

import com.nyf.constant.CrowdConstant;
import com.nyf.entity.Admin;
import com.nyf.exception.AccessForbiddenException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session对象
        HttpSession session = request.getSession();
        //尝试从session中获取admin
        Admin admin = (Admin) session.getAttribute(CrowdConstant.LOGIN_ADMIN_NAME);
        //判断admin是否为空
        if (admin==null){
            //抛出异常
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }
        return true;
    }
}
