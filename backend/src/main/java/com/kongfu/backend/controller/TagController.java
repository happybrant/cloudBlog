package com.kongfu.backend.controller;

import com.kongfu.backend.entity.Category;
import com.kongfu.backend.entity.ObjectRespBean;
import com.kongfu.backend.entity.PageModel;
import com.kongfu.backend.entity.Tag;
import com.kongfu.backend.service.CategoryService;
import com.kongfu.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/{id}")
    @ResponseBody
    public ObjectRespBean getTagById(@PathVariable("id") int id){
        return ObjectRespBean.returnSuccess(tagService.findTagById(id));
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

    /**
     * 新增标签
     * @param tag
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ObjectRespBean addTag(Tag tag){
        //根据名称查找分类，用于判断分类是否存在
        String name = tag.getName();
        Tag exist = tagService.findTag(name);
        if(tag.getId() == null){
            if(exist != null){
                return ObjectRespBean.returnFail("当前名称的标签已存在");
            }
            //新增标签
            tagService.addTag(tag);
        }
        else{
            if (!exist.getId().equals(tag.getId())) {
                //如果当前的名字和其他标签重复提示用户
                return ObjectRespBean.returnFail("当前名称的标签已存在");
            }else{
                //更新标签
                tagService.updateTag(tag);
            }
        }
        int id = tag.getId();
        return ObjectRespBean.returnSuccess(id);
    }

    /**
     * 删除标签
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ObjectRespBean deleteTags(@RequestBody int[] ids){
        tagService.deleteTags(ids);
        return ObjectRespBean.returnSuccess();
    }


    /**
     * 修改标签状态
     * @param tag
     * @return
     */
    @RequestMapping("changeState")
    @ResponseBody
    public ObjectRespBean changeState(Tag tag){
        tagService.updateTag(tag);
        return ObjectRespBean.returnSuccess();
    }

    /**
     * 上下移动顺序
     * @param map
     * @return
     */
    @RequestMapping("move")
    @ResponseBody
    public ObjectRespBean moveOrder(@RequestBody  Map<String, String> map){
        int id = Integer.parseInt(map.get("id"));
        String direction = map.get("direction");
        return tagService.moveOrder(id, direction);
    }

}
