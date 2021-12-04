package com.kongfu.backend.Config;

import com.kongfu.backend.entity.LoginTicket;
import com.kongfu.backend.entity.User;
import com.kongfu.backend.service.UserService;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.BlogUtil;
import com.kongfu.backend.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author 付聪
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements BlogConstant {

    private final  String XMLHttpRequest = "XMLHttpRequest";
    @Autowired
    private UserService userService;
    /**
     * 静态资源
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //由于出现权限判断会在拦截器前执行，故在权限判定前面再加一层过滤器
        http.addFilterBefore((servletRequest, servletResponse, chain) -> {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String ticket = CookieUtil.getValue(request, "ticket");
            BlogUtil.setContext(ticket, userService);
            // 让请求继续向下执行.
            chain.doFilter(request, response);
        }, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers(
                        "/index",
                        "/article/**",
                        "/tag/**",
                        "/category/**"
                )
                .hasAnyAuthority(
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN
                )
                .anyRequest().permitAll()
                //关闭csrf防护，类似于防火墙，不关闭上面的设置不会真正生效
                .and().csrf().disable();

        // 权限不够时的处理
        http.exceptionHandling()
                // 1. 未登录时的处理
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if (XMLHttpRequest.equals(xRequestedWith)) {
                            // 异步请求
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(BlogUtil.getJSONString(403, "你还没有登录"));
                        }
                        else {
                            // 普通请求
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                // 2. 权限不够时的处理
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if (XMLHttpRequest.equals(xRequestedWith)) {
                            // 异步请求
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(BlogUtil.getJSONString(403, "你没有访问该功能的权限"));
                        }
                        else {
                            // 普通请求
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        // Security 底层会默认拦截 /logout 请求，进行退出处理
        // 此处赋予它一个根本不存在的退出路径，使得程序能够执行到我们自己编写的退出代码
        http.logout().logoutUrl("/securitylogout");

        http.headers().frameOptions().sameOrigin();

    }
}
