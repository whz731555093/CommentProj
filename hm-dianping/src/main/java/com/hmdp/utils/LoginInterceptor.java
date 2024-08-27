package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.LOGIN_TOKEN_KEY;
import static com.hmdp.utils.RedisConstants.LOGIN_TOKEN_TTL;

/**
 * @description 拦截器2，拦截需要登录的路径
 * 1.查询 ThreadLocal 的用户
 * 不存在，则拦截
 * 存在，则放行
 */
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * @description 前置拦截，从session中获取数据
     * @param request
     * @param response
     * @param handler
     * @return false-拦截；ture-放行
     * @throws Exception
     */
    public boolean preHandleBySession(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
     * @description 拦截器2的主要功能
     * @param request
     * @param response
     * @param handler
     * @return false-拦截；ture-放行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.判断是否需要拦截（ThreadLocal 中是否有用户）
        if (UserHolder.getUser() == null) {
            // 不存在，需要拦截，返回状态码401
            response.setStatus(401);
            return false;
        }
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
}
