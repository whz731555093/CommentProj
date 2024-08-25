package com.hmdp.utils;

import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * @description 前置拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // // 1.判断是否需要拦截（ThreadLocal中是否有用户）
        // if (UserHolder.getUser() == null) {
        //     // 没有，需要拦截，设置状态码
        //     response.setStatus(401);
        //     // 拦截
        //     return false;
        // }
        // // 有用户，则放行
        // return true;

        // 1.获取session
        HttpSession session = request.getSession();

        // 2.获取session中的用户
        Object user = session.getAttribute("user");

        // 3.判断用户是否存在
        if (user == null) {
            // 4.不存在则进行拦截，并返回401
            response.setStatus(401);
            return false;
        }

        // 5.存在则保存用户信息到ThreadLocal
        UserHolder.saveUser((UserDTO) user);

        // 6.放行
        return true;
    }

    /**
     * @description controller执行之后进行拦截
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * @description 渲染之后进行拦截
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}
