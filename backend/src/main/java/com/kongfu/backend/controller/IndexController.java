package com.kongfu.backend.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {


    @GetMapping("/index")
    public String getIndexPage( Model model){
        //分页

        return "index";
    }
    @GetMapping("/edit")
    public String getEditPage( Model model){
        //分页
        return "/site/article-list";
    }
}
