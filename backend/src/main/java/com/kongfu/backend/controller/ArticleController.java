package com.kongfu.backend.controller;


import com.ctc.wstx.util.StringUtil;
import com.kongfu.backend.entity.*;
import com.kongfu.backend.service.ArticleService;
import com.kongfu.backend.service.CategoryService;
import com.kongfu.backend.service.TagService;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.BlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author 付聪
 */
@RequestMapping("/article")
@Controller
public class ArticleController {
    @Autowired
    public ArticleService articleService;
    @Autowired
    public CategoryService categoryService;
    @Autowired
    public TagService tagService;

    /**
     * 博客发布页面
     * @param model
     * @return
     */
    @GetMapping("/publish")
    public String getArticlePublish(Model model){
        //分类
        List<Category> categoryList = categoryService.findCategories();
        Category category = new Category();
        category.setName("请选择父分类");
        //加一个全部的选项，方便前端选择
        categoryList.add(0,category);
        model.addAttribute("categories",categoryList);
        //标签
        int count = tagService.findTagCount();
        List<Tag> tagList = tagService.findAllTags(0, count);
        model.addAttribute("tags",tagList);
        return "/site/article-publish";
    }
    /**
     * 博客列表页面
     * @return
     */
    @RequestMapping("/list")
    public  String List(Model model){
        //分类
        List<Category> categoryList = categoryService.findCategories();
        Category category = new Category();
        category.setName("请选择父分类");
        //加一个全部的选项，方便前端选择
        categoryList.add(0,category);
        model.addAttribute("categories",categoryList);
        //标签
        int count = tagService.findTagCount();
        List<Tag> tagList = tagService.findAllTags(0, count);
        Tag tag = new Tag();
        tag.setName("请选择标签");
        tagList.add(0,tag);
        model.addAttribute("tags",tagList);
        return "/site/article-list";
    }
    /**
     * 博客编辑页面
     * @return
     */
    @RequestMapping("/edit/{id}")
    public  String Edit(Model model, @PathVariable("id") int id){
        //获取该博客的详情
        Article article = articleService.findArticleById(id);
        if(article == null){
            return "/error/404";
        }
        model.addAttribute("article",article);
        //分类
        List<Category> categoryList = categoryService.findCategories();
        Category category = new Category();
        category.setName("请选择父分类");
        //加一个全部的选项，方便前端选择
        categoryList.add(0,category);
        //获取父分类
        String path = article.getPath();
        String rootCategoryName = path.split("/")[1];
        Category rootCategory = categoryList.stream().filter(s->s.getName().equals(rootCategoryName)).collect(Collectors.toList()).get(0);
        model.addAttribute("rootCategory",rootCategory);
        //移除已被选中的父分类
        categoryList.removeIf(item -> item.getName().equals(rootCategoryName));
        model.addAttribute("categories",categoryList);
        //如果有子分类，获取子分类
        if(path.split("/").length > 2){
            String childrenCategoryName = path.split("/")[2];
            Category childrenCategory = categoryService.findCategory(childrenCategoryName);
            List<Category> childrenCategories  = categoryService.findChildrenCategories(rootCategoryName);
            model.addAttribute("childrenCategory",childrenCategory);
            //移除已被选中的子分类
            childrenCategories.removeIf(item -> item.getName().equals(childrenCategoryName));
            model.addAttribute("childrenCategories",childrenCategories);
        }
        //标签
        int count = tagService.findTagCount();
        List<Tag> tagList = tagService.findAllTags(0, count);
        //获取已被选中的标签id
        List<Integer> selectIds = new ArrayList<>();
        if(article.getTags() != null && article.getTags().size() > 0){
            for(Tag tag: article.getTags()){
                selectIds.add(tag.getId());
            }
        }
        //移除已被选中的标签
        tagList.removeIf(item -> selectIds.contains(item.getId()));
        model.addAttribute("tags",tagList);
        return "/site/article-publish";
    }

    /**
     * 发布/暂存文章
     * @param article
     * @return
     */
    @PostMapping("/add/{status}")
    @ResponseBody
    public String addArticle(Article article, @PathVariable("status") int status) {
        if (status == 1) {
            //发布文章
            article.setStatus(BlogConstant.PUBLISH_STATUS);
        } else {
            //暂存文章
            article.setStatus(BlogConstant.UN_PUBLISH_STATUS);
        }
        int[] tagIds = article.getTagIds();
        //插入文章
        articleService.addArticle(article);
        List<TagArticle> tagArticles = new ArrayList<>();
        if (tagIds != null && tagIds.length > 0) {
            //存在标签
            for (int i = 0; i < tagIds.length; i++) {
                TagArticle tagArticle = new TagArticle(tagIds[i], article.getId());
                tagArticles.add(tagArticle);
            }
            //插入文章标签关联表
            tagService.insertTagArticle(tagArticles);
        }
        return BlogUtil.getJSONString(0, "发布成功");
    }


    /**
     * 根据条件查询博客
     * @param request
     * @return
     */
    @GetMapping("/getArticleList")
    @ResponseBody
    public PageModel getArticleList(HttpServletRequest request){
        int offset = Integer.parseInt(request.getParameter("offset"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        String category = request.getParameter("category") == null ? "all" : request.getParameter("category");
        String tag = request.getParameter("tag") == null ? "all" : request.getParameter("tag");
        String createDate = request.getParameter("createDate") == null ? "all" : request.getParameter("createDate");
        String title = request.getParameter("title") == null ? "" : request.getParameter("title");
        List<Article> articleList = articleService.findArticles(category, createDate, tag, title , offset, limit);
        PageModel pageModel = new PageModel();
        //总数
        int count = articleService.findArticleCount(category,createDate, tag, title);
        pageModel.setTotal(count);
        pageModel.setRows(articleList);
        return pageModel;
    }

    /**
     * 根据博客id删除博客
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public  ObjectRespBean DeleteArticle(@PathVariable("id") int id){
        articleService.deleteArticle(id);
        return ObjectRespBean.returnSuccess();
    }
}
