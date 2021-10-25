package com.kongfu.backend.controller;

import com.ctc.wstx.util.StringUtil;
import com.kongfu.backend.entity.Category;
import com.kongfu.backend.entity.ObjectRespBean;
import com.kongfu.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * @author 付聪
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    public CategoryService categoryService;

    /**
     * 分类页面
     * @return
     */
    @RequestMapping("/list")
    public String List(){
        return "/site/category";
    }

    /**
     * 获取分类根节点
     * @return
     */
    @RequestMapping("getRootCategories")
    @ResponseBody
    public ObjectRespBean getRootCategories(){
        List<Category> categories = categoryService.findCategories();
        return ObjectRespBean.returnSuccess(categories);
    }

    /**
     * 根据父分类获取子分类
     * @param rootCategory
     * @return
     */
    @RequestMapping("getChildrenCategories/{rootCategory}")
    @ResponseBody
    public ObjectRespBean getChildrenCategories(@PathVariable("rootCategory") String rootCategory){
        return ObjectRespBean.returnSuccess(categoryService.findChildrenCategories(rootCategory));
    }

    /**
     * 移动分类
     * @param type
     * @param name
     */
    @RequestMapping("move")
    @ResponseBody
    public ObjectRespBean move(@RequestParam("type") String type,@RequestParam("name") String name){
        List<Category> siblingCategories = categoryService.findSiblingCategories(name);
        if(siblingCategories != null && siblingCategories.size() > 0){
            for (int i = 0; i < siblingCategories.size(); i++){
                Category category = siblingCategories.get(i);
                if(category.getName().equals(name)){
                    if(type.equals("up")){
                        //获取上一个节点
                        if(i == 0){
                            return ObjectRespBean.returnFail("首节点无法上移");
                        }else{
                            Category pre = siblingCategories.get(i- 1);
                            pre.setOrder(pre.getOrder() + 1);
                            category.setOrder(category.getOrder() - 1);
                            categoryService.updateCategory(category);
                            categoryService.updateCategory(pre);
                        }
                    }
                    else{
                        if(i == siblingCategories.size() - 1){
                            return ObjectRespBean.returnFail("尾节点无法下移");
                        }else{
                            Category next = siblingCategories.get(i + 1);
                            next.setOrder(next.getOrder() - 1);
                            category.setOrder(category.getOrder() + 1);
                            categoryService.updateCategory(category);
                            categoryService.updateCategory(next);
                        }
                    }
                    break;
                }
            }
        }
        return ObjectRespBean.returnSuccess();
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ObjectRespBean updateCategory(Category category){
        //根据名称查找分类，用于判断分类是否存在
        String name = category.getName();
        Category exist = categoryService.findCategory(name);
        if(exist != null){
            if(exist.getId() != category.getId()){
                return ObjectRespBean.returnFail("当前名称的分类已存在");
            }
        }
        categoryService.updateCategory(category);
        return ObjectRespBean.returnSuccess();
    }
    /**
     * 新增分类
     * @param category
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ObjectRespBean addCategory(Category category){
        //根据名称查找分类，用于判断分类是否存在
        String name = category.getName();
        Category exist = categoryService.findCategory(name);
        if(exist != null){
            return ObjectRespBean.returnFail("当前名称的分类已存在");
        }
        //查找父分类的路径
        int parentId = category.getParentId();
        Category parentCategory = categoryService.findCategoryById(parentId);
        String path = "";
        if(parentCategory == null){
            path = "/" + category.getName();
        }
        else {
            path = parentCategory.getPath() + "/" + category.getName();
        }
        category.setPath(path);
        categoryService.addCategory(category);
        int id = category.getId();
        return ObjectRespBean.returnSuccess(id);
    }
}
