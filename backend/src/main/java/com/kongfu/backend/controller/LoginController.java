package com.kongfu.backend.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author 付聪
 */
@Controller
public class LoginController {

    /**
     * 进入登陆界面
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return "site/login";
    }

}