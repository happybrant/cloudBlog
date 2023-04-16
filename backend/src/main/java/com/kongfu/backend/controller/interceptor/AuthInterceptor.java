package com.kongfu.backend.controller.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.kongfu.backend.annotation.Authentication;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.model.vo.LoginToken;
import com.kongfu.backend.util.BlogConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * @author 付聪
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private HostHolder holder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.trace("ApiInterceptor:" + request.getRequestURI());

        String authorization = "";
        // 暴露自定义的header,设置此值，才能在js中的response的header属性中可见
        response.setHeader("Access-Control-Expose-Headers", "auth");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        authorization = request.getHeader("authorization");
        if (StringUtils.isEmpty(authorization) || authorization.length() <= 6) {
            response.addHeader("auth", "Token error");
            response.getWriter().append(handleError("Token 错误").toString());

            return false;
        }
        if (!"bearer ".equals(authorization.substring(0, 7).toLowerCase())) {
            response.addHeader("auth", "Token error");
            response.getWriter().append(handleError("Token 错误").toString());

            return false;
        }
        LoginToken token = LoginToken.checkTicket(authorization.substring(7));
        if (token == null) {
            response.addHeader("auth", "Token error");
            response.getWriter().append(handleError("Token 错误").toString());

            return false;
        }
        if (new Date().after(token.getExpiration())) {
            response.setDateHeader("expires", -1);
            // 登录超时
            response.addHeader("auth", "login expire");
            response.getWriter().append(handleError("登录超时").toString());

            return false;
        }
        // 如果在黑名单中表名该用户已经退出登陆了
        if (Objects.equals(
                redisTemplate.hasKey(BlogConstant.TOKEN_BLACKLIST_PREFIX + token.getJti()), Boolean.TRUE)) {
            response.addHeader("auth", "token 失效");
            response.getWriter().append(handleError("token 失效").toString());
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 获取当前方法上的指定注解
        Authentication auth = method.getAnnotation(Authentication.class);
        if (auth != null) {
            // 当菜单权限级别高于该用户级别时，不允许访问接口，注：数字越小，权限越高
            if (token.getRole() > auth.role()) {
                response.addHeader("auth", "菜单【" + auth.menu() + "】权限不够");
                response.getWriter().append(handleError("菜单【" + auth.menu() + "】权限不够").toString());
                return false;
            }
        }

        holder.setUser(token);
        return true;
    }

    /**
     * 处理报错信息
     *
     * @param message
     * @return
     */
    public JSONObject handleError(String message) {
        JSONObject res = new JSONObject();
        res.put("success", false);
        res.put("code", 401);
        res.put("message", message);
        return res;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
