package com.kongfu.backend.controller;
import com.kongfu.backend.entity.ObjectRespBean;
import com.kongfu.backend.entity.User;
import com.kongfu.backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import  com.kongfu.backend.util.BlogConstant ;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @author 付聪
 */
@Controller
public class LoginController implements BlogConstant {

    @Autowired
    private UserService userService;

    @Value("${server.servlet.context-path}")
    private String contextPath;
    /**
     * 进入登陆界面
     * @return
     */
    @GetMapping("/login")
    public String getLoginPage() {
        return "site/login";
    }

    /**
     * 用户登陆
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ObjectRespBean login(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
                                HttpServletResponse response){
        // 凭证过期时间（是否记住我）
        int expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        // 验证用户名和密码
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey(TICKET)){
            // 账号和密码均正确，则服务端会生成 ticket，浏览器通过 cookie 存储 ticket
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            //cookie有效范围
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return ObjectRespBean.returnSuccess();
        }
        else{
            String failMsg= map.get("usernameMsg") == null?map.get("passwordMsg").toString():map.get("usernameMsg").toString();
            return ObjectRespBean.returnFail(failMsg);
        }
    }

}