package com.hik.practicedemo.conf;

import com.hik.practicedemo.aop.AccessLimitInterceptor;
import com.hik.practicedemo.aop.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Created by wangJinChang on 2019/12/28 11:28
 * 拦截器配置类
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private AccessLimitInterceptor accessLimitInterceptor;

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor)
                .addPathPatterns("/qr/**")          // 拦截路径
                .excludePathPatterns("/user");      // 不拦截路径

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/qr/**")
                .excludePathPatterns("/user");
    }
}
