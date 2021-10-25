package com.kongfu.frontend.controller;

import com.kongfu.frontend.entity.*;
import com.kongfu.frontend.service.ArticleService;
import com.kongfu.frontend.service.CategoryService;
import com.kongfu.frontend.service.TagService;
import com.kongfu.frontend.util.BlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    public ArticleService articleService;
    @Autowired
    public CategoryService categoryService;
    @Autowired
    public TagService tagService;
    @GetMapping(value = {"/index","/index/page/{pageNum}"})
    public String getIndexPage(@PathVariable(value = "pageNum", required = false) Integer pageNum, Model model){
        //分页
        Page page = new Page();
        if(pageNum == null){
            pageNum = 1;
        }
        page.setCurrent(pageNum);
        page.setPath("/index");
        int count = articleService.findArticleCount("all","all", "all");
        page.setRows(count);
        model.addAttribute("page",page);
        //博客
        List<Article> articleList = articleService.findArticles("all","all", "all", page.getOffset(),page.getLimit());
        model.addAttribute("articles",articleList);
        addBaseAttribute(model);
        return "index";
    }
    /**
     * 博客分类
     * @param rootCategory
     * @param category
     * @param model
     * @return
     */
    @GetMapping(value={"/categories","/categories/{rootCategory}/{category}","/categories/{rootCategory}","/categories/{rootCategory}/{category}/page/{pageNum}","/categories/{rootCategory}/page/{pageNum}"})
    public String  getCategoryArticles(@PathVariable(value = "rootCategory", required = false) String rootCategory, @PathVariable(value = "category", required = false) String category, @PathVariable(value = "pageNum", required = false) Integer pageNum, Model model){
        if(rootCategory != null){
            //分页
            Page page = new Page();
            if(pageNum == null){
                pageNum = 1;
            }
            page.setCurrent(pageNum);
           int count = 0;
            //博客
            List<Article> articleList;
            if(category != null){
                count = articleService.findArticleCount(category,"all", "all");
                articleList = articleService.findArticles(category, "all", "all",page.getOffset(), page.getLimit());
                page.setPath("/categories/" + rootCategory + "/" + category);
            }else {
                count = articleService.findArticleCount(rootCategory,"all", "all");
                articleList =  articleService.findArticles(rootCategory, "all", "all", page.getOffset(), page.getLimit());
                page.setPath("/categories/" + rootCategory);
            }
            page.setRows(count);
            model.addAttribute("page",page);
            model.addAttribute("articles",articleList);
            addBaseAttribute(model);
            return  "site/category";
        }
        else{
            addBaseAttribute(model);
            return  "site/categories";
        }
    }
    /**
     * 博客归档
     * @param year
     * @param month
     * @param model
     * @return
     */
    @GetMapping(value={"/archives/{year}/{month}"})
    public String  getArchiveArticles(@PathVariable("year") String year, @PathVariable("month") String month, Model model){
        List<Article> articleList = articleService.findArticles("all",year + "-" + month, "all", 0, 10);
        model.addAttribute("articles",articleList);
        model.addAttribute("dateTime",BlogUtil.int2chineseNum(Integer.parseInt(month)) + "月 " + year);
        addBaseAttribute(model);
        return "site/archive";
    }

    /**
     * 博客标签
     * @param tag
     * @param pageNum
     * @param model
     * @return
     */
    @GetMapping(value={"/tags","/tags/{tag}","/tags/{tag}/page/{pageNum}"})
    public String  getTagArticles(@PathVariable(value = "tag", required = false) String tag, @PathVariable(value = "pageNum", required = false) Integer pageNum, Model model){
        if(tag != null){
            //分页
            Page page = new Page();
            if(pageNum == null){
                pageNum = 1;
            }
            page.setCurrent(pageNum);
            page.setPath("/tags/" + tag);
            int count = articleService.findArticleCount("all","all", tag);
            page.setRows(count);
            model.addAttribute("page",page);
            List<Article> articleList = articleService.findArticles("all","all", tag, page.getOffset(), page.getLimit());
            model.addAttribute("articles",articleList);
            addBaseAttribute(model);
            return "site/tag";
        }
        else{
            addBaseAttribute(model);
            return "site/tags";
        }
    }
    /**
     * 添加基础属性
     * @param model
     */
    public void addBaseAttribute(Model model){
        int totalCount = articleService.findArticleCount("all","all", "all");
        List<Article> articleList = articleService.findArticles("all","all", "all", 0,totalCount);
        //分类
        List<Category> categoryList = categoryService.findCategories();
        model.addAttribute("categories",categoryList);
        //归档--对月份进行分组
        Map<String, List<Article>> month = articleList.stream().collect(Collectors.groupingBy(p -> BlogUtil.int2chineseNum(Integer.parseInt(p.getCreateTime().split("-")[1])) + "月 " + p.getCreateTime().split("-")[0]));
        Map<String, Archive> unSortedArchiveMap = new HashMap<>();
        month.forEach((key, value) -> {
            Archive archive = new Archive(value.get(0).getCreateTime().split("-")[0], value.get(0).getCreateTime().split("-")[1], value.size());
            unSortedArchiveMap.put(key, archive);
        });
        //对归档进行倒序排序
        Map<String, Archive> archiveMap = unSortedArchiveMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(p -> -Integer.parseInt(p.getYear()) * 12 - Integer.parseInt(p.getMonth()))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        model.addAttribute("archives",archiveMap);
        //标签
        List<Tag> tagList = tagService.findTags();
        model.addAttribute("tags",tagList);
        //统计
        model.addAttribute("articleCount",articleList.size());
        model.addAttribute("categoryCount",categoryList.size());
        model.addAttribute("tagCount",tagList.size());
    }
}
