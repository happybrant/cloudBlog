package com.kongfu.backend.Config;

import com.kongfu.backend.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 付聪
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    /**
     *对除静态资源的所有路径进行拦截
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/assets/**", "/login/**", "/img/**", "/editor-md/**", "/editor-md-upload/**","/toastr/**")
                //同一请求被拦截两次处理方法
                .excludePathPatterns("/error")
                .addPathPatterns("/**");
    }
    // 配置虚拟路径映射访问
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // System.getProperty("user.dir") 获取程序的当前路径
        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\editor-md-upload\\";
        registry.addResourceHandler("/editor-md-upload/**").addResourceLocations("file:" + path);
    }

}
