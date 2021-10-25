package com.kongfu.frontend.controller;

import com.kongfu.frontend.entity.*;
import com.kongfu.frontend.service.ArticleService;
import com.kongfu.frontend.service.CategoryService;
import com.kongfu.frontend.service.TagService;
import com.kongfu.frontend.util.BlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
     * 文章详情页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/detail/{id}")
    public String getArticleDetail(@PathVariable("id") int id, Model model){
        Article article = articleService.findArticleById(id);
        model.addAttribute("article",article);
        addBaseAttribute(model);
        return "site/article-detail";
    }

    /**
     * 文章发布页面
     * @param model
     * @return
     */
    @GetMapping("/publish")
    public String getArticlePublish(Model model){
        //分类
        List<Category> categoryList = categoryService.findCategories();
        model.addAttribute("categories",categoryList);
        //标签
        List<Tag> tagList = tagService.findAllTags();
        model.addAttribute("tags",tagList);
        return "site/article-publish";
    }


    /**
     * 新增博客
     * @param article
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public String addArticle(Article article) {
        int[] tagIds = article.getTagIds();
        //插入文章
        articleService.addArticle(article);

        List<TagArticle> tagArticles = new ArrayList<>();
        for (int i = 0; i < tagIds.length; i++) {
            TagArticle tagArticle = new TagArticle(tagIds[i],article.getId());
            tagArticles.add(tagArticle);
        }
        //插入文章标签关联表
        tagService.insertTagArticle(tagArticles);
        return BlogUtil.getJSONString(0, "发布成功");
    }

    @GetMapping("/articles")
    public List<Article> getAllArticles(){
        return articleService.findArticles("all", "all","all",0,10);
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

    /**
     * 添加帖子（发帖）
     * @param title
     * @param content
     * @return
     */
   /* @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "您还未登录");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());

        discussPostService.addDiscussPost(discussPost);

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(discussPost.getId());
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, discussPost.getId());

        return CommunityUtil.getJSONString(0, "发布成功");
    }*/
}
