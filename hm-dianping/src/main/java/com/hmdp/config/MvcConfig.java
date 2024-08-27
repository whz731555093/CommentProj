package com.hmdp.config;

import com.hmdp.utils.LoginInterceptor;
import com.hmdp.utils.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @description 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 默认所有拦截器的order都为0，按照注册顺序执行，也可按以下方式修改
        // 拦截器2：登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/shop/**", // 查询店铺信息
                        "/voucher/**",  // 查询优惠券信息
                        "/shop-type/**",    // 查询店铺类型
                        "/upload/**",   // 上传数据 理论上要拦截
                        "/blog/hot",    // 查询热点数据
                        "/user/code",   // 验证码url
                        "/user/login"   // 登录url
                ).order(1);
        // 拦截器1：token刷新的拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
    }
}
