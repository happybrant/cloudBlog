package com.kongfu.backend.controller;

import com.kongfu.backend.entity.Category;
import com.kongfu.backend.entity.ObjectRespBean;
import com.kongfu.backend.entity.PageModel;
import com.kongfu.backend.entity.Tag;
import com.kongfu.backend.service.CategoryService;
import com.kongfu.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 付聪
 */
@Controller
@RequestMapping("/tag")
public class TagController {
    @Autowired
    public TagService tagService;

    /**
     * 标签页面
     * @return
     */
    @RequestMapping("/list")
    public String List(){
        return "/site/tag";
    }

    /**
     * 获取所有标签
     * @return
     */
    @RequestMapping("getTags")
    @ResponseBody
    public PageModel getTags(HttpServletRequest request){
        int offset = Integer.parseInt(request.getParameter("offset"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        List<Tag> tags = tagService.findAllTags(offset, limit);
        //标签总数
        int count = tagService.findTagCount();
        PageModel pageModel = new PageModel();
        pageModel.setRows(tags);
        pageModel.setTotal(count);
        return pageModel;
    }
}
