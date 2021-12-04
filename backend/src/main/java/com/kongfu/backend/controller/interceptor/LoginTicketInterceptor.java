package com.kongfu.backend.controller.interceptor;

import com.kongfu.backend.entity.HostHolder;
import com.kongfu.backend.entity.LoginTicket;
import com.kongfu.backend.entity.User;
import com.kongfu.backend.service.UserService;
import com.kongfu.backend.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author 付聪
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder holder;

    @Autowired
    private UserService userService;

    /**
     * 在 Controller 执行之前被调用
     * 检查凭证状态，若凭证有效则在本次请求中持有该用户信息
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中获取凭证
        String ticket = CookieUtil.getValue(request,"ticket");
        if(ticket != null){
            //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //检查凭证状态（是否）是否过期
            if(loginTicket != null && loginTicket.getStatus() == 1 && loginTicket.getExpired().after(new Date())){
                User user = userService.findUserById(loginTicket.getUserId());
                holder.setUser(user);
                // 构建用户认证的结果，并存入 SecurityContext, 以便于 Spring Security 进行授权
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId())
                );
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
        return true;
    }

    /**
     * 在模板引擎之前被调用
     * 将用户信息存入 modelAndView, 便于模板引擎调用
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = holder.getUser();
        if(user != null && modelAndView != null){
            modelAndView.addObject("loginUser", user);
        }
    }

    /**
     * 在 Controller 执行之后（即服务端对本次请求做出响应后）被调用
     * 清理本次请求持有的用户信息
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        holder.clear();
        SecurityContextHolder.clearContext();
    }
}
