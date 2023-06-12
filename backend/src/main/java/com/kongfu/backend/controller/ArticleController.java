package com.kongfu.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.ArticleQuery;
import com.kongfu.backend.model.entity.Article;
import com.kongfu.backend.model.entity.TagArticle;
import com.kongfu.backend.service.ArticleService;
import com.kongfu.backend.service.TagService;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.MapUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author 付聪 */
@RequestMapping("/article")
@RestController
public class ArticleController implements BlogConstant {
  @Resource public ArticleService articleService;
  @Resource public TagService tagService;
  @Resource public RabbitTemplate rabbitTemplate;
  @Resource private RestTemplate restTemplate;

  /**
   * 博客列表
   *
   * @return
   */
  @RequestMapping("/list")
  @Log(menu = "博客管理", description = "获取博客列表")
  public ResponseResult<Page<Article>> list(
      @RequestBody(required = false) Map<String, Object> map) {
    Page<Article> albumPage = articleService.getArticleListPager(getArticleQuery(map));
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", albumPage);
  }

  /**
   * 接口查询条件
   *
   * @param map
   * @return
   */
  private ArticleQuery getArticleQuery(Map<String, Object> map) {
    ArticleQuery query = new ArticleQuery();

    if (map == null || map.size() == 0) {
      query.setPageIndex(0);
      query.setPageSize(Integer.MAX_VALUE);
    } else {
      int pageIndex = MapUtil.getValueAsInteger(map, "pageIndex", 1);
      int pageSize = MapUtil.getValueAsInteger(map, "pageSize", 10);
      query.setPageSize(pageSize);
      query.setPageIndex(pageIndex);
      query.setStartRow(pageSize * (pageIndex - 1));
      query.setStatus(MapUtil.getValueAsInteger(map, "status", null));
      query.setCategoryId(MapUtil.getValueAsInteger(map, "categoryId", null));
      query.setTitle(MapUtil.getValueAsString(map, "title"));
      query.setCreateTime(MapUtil.getValueAsString(map, "createTime"));
    }

    return query;
  }

  /**
   * 根据id获取博客
   *
   * @param id
   * @return
   */
  @GetMapping("/getArticleById")
  @Log(menu = "博客管理", description = "根据id获取博客")
  public ResponseResult<Article> getArticleById(@RequestParam("id") int id) {
    if (id <= 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    Article article = articleService.getArticleById(id);
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", article);
  }
  /**
   * 获取当前用户的关于我文档
   *
   * @return
   */
  @GetMapping("/getAboutMeArticle")
  @Log(menu = "博客管理", description = "获取当前用户的关于我文档")
  public ResponseResult<Article> getAboutMeArticle() {
    Article article = articleService.getAboutMeArticle();
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", article);
  }
  /**
   * /** 发布/暂存文章
   *
   * @param article
   * @return
   */
  @PostMapping("/add")
  @Log(menu = "博客管理", description = "发布/暂存文章")
  public ResponseResult<String> addArticle(@RequestBody Article article) {
    ResponseResult<String> result;
    if (article == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = articleService.addArticle(article);
    if (i > 0) {
      // 关于我的文章不需进行下面操作
      if (article.getStatus() != BlogConstant.ABOUT_ME_STATUS
          && article.getStatus() != BlogConstant.ABOUT_ME_UN_PUBLISH_STATUS) {
        // 插入文章
        int[] tagIds = article.getTagIds();

        List<TagArticle> tagArticles = new ArrayList<>();
        if (tagIds != null && tagIds.length > 0) {
          // 存在标签
          for (int tagId : tagIds) {
            TagArticle tagArticle = new TagArticle(tagId, article.getId());
            tagArticles.add(tagArticle);
          }
          // 插入文章标签关联表
          tagService.insertTagArticle(tagArticles);
        }
        // 通过消息队列将其存入 Elasticsearch 服务器
        Map<String, Object> map = new HashMap<>(16);
        map.put("articleId", article.getId());
        map.put("type", "insert");
        rabbitTemplate.convertAndSend("article", map);
        // 更新缓存
        String url = "http://localhost:8080/testGet";
        // 发起请求,直接返回对象
        String responseBean = restTemplate.getForObject(url, String.class);
        System.out.println(responseBean);
      }
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  @RequestMapping("/test")
  public void test() {
    String url = "http://localhost:8084/home/refreshStatisticCache?router=" + "scurry";
    // 发起请求,直接返回对象
    String responseBean = restTemplate.getForObject(url, String.class);
    System.out.println(responseBean);
  }

  /**
   * 修改博客
   *
   * @param article
   * @return
   */
  @PostMapping("/update")
  @Log(menu = "博客管理", description = "修改博客")
  public ResponseResult<String> updateArticle(@RequestBody Article article) {
    ResponseResult<String> result;

    if (article == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    // 修改文章
    int i = articleService.updateArticle(article);
    if (i > 0) {
      // 关于我的文章不需进行下面操作
      if (article.getStatus() != BlogConstant.ABOUT_ME_STATUS
          && article.getStatus() != BlogConstant.ABOUT_ME_UN_PUBLISH_STATUS) {
        // 删除原有的标签
        tagService.deleteTagArticle(article.getId());
        // 获取新的标签
        int[] tagIds = article.getTagIds();
        List<TagArticle> tagArticles = new ArrayList<>();
        if (tagIds != null && tagIds.length > 0) {
          // 存在标签
          for (int tagId : tagIds) {
            TagArticle tagArticle = new TagArticle(tagId, article.getId());
            tagArticles.add(tagArticle);
          }
          // 插入文章标签关联表
          tagService.insertTagArticle(tagArticles);
        }
        // 通过消息队列将其存入 Elasticsearch 服务器
        Map<String, Object> map = new HashMap<>(16);
        map.put("articleId", article.getId());
        map.put("type", "update");
        rabbitTemplate.convertAndSend("article", map);
      }
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 删除博客
   *
   * @param params
   * @return
   */
  @PostMapping("/delete")
  @Log(menu = "博客管理", description = "删除博客")
  public ResponseResult<String> deleteArticle(@RequestBody List<Integer> params) {
    ResponseResult<String> result;
    if (params == null || params.size() == 0) {
      result = new ResponseResult<>(ResponseResultCode.ParameterEmpty, "传入参数为空");
    } else {
      int i = articleService.deleteArticle(params);
      if (i > 0) {
        // 通过消息队列将其存入 Elasticsearch 服务器
        for (int id : params) {
          Map<String, Object> map = new HashMap<>(16);
          map.put("articleId", id);
          map.put("type", "delete");
          rabbitTemplate.convertAndSend("article", map);
        }
        result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
      } else {
        result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
      }
    }
    return result;
  }
}
