package com.framework.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.backend.annotation.Log;
import com.framework.backend.common.ResponseData;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.dto.ArticleQuery;
import com.framework.backend.model.entity.Article;
import com.framework.backend.model.entity.User;
import com.framework.backend.service.ArticleService;
import com.framework.backend.service.SettingService;
import com.framework.backend.util.BlogConstant;
import com.framework.backend.util.MapUtil;
import com.framework.backend.utils.SecurityUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author 付聪
 */
@RequestMapping("/article")
@RestController
@Slf4j
@ResponseResult
public class ArticleController implements BlogConstant {
  @Autowired public ArticleService articleService;

  @Value("${restTemplate.url}")
  private String restTemplateUrl;

  @Autowired public RabbitTemplate rabbitTemplate;

  @Autowired private RestTemplate restTemplate;
  @Autowired private SettingService settingService;

  /**
   * 博客列表
   *
   * @return
   */
  @RequestMapping("/list")
  public Page<Article> list(@RequestBody(required = false) Map<String, Object> map) {
    return articleService.getArticleListPager(getArticleQuery(map));
  }

  /**
   * 接口查询条件
   *
   * @param map
   * @return
   */
  private ArticleQuery getArticleQuery(Map<String, Object> map) {
    ArticleQuery query = new ArticleQuery();

    if (map == null || map.isEmpty()) {
      query.setPageIndex(0);
      query.setPageSize(Integer.MAX_VALUE);
    } else {
      int pageIndex = MapUtil.getValueAsInteger(map, "pageIndex", 1);
      int pageSize = MapUtil.getValueAsInteger(map, "pageSize", 10);
      query.setPageSize(pageSize);
      query.setPageIndex(pageIndex);
      query.setStartRow(pageSize * (pageIndex - 1));
      query.setStatus(MapUtil.getValueAsInteger(map, "status", null));
      query.setCategoryId(MapUtil.getValueAsString(map, "categoryId", null));
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
  public Article getArticleById(@RequestParam("id") String id) {
    return articleService.getArticleById(id);
  }

  /**
   * 获取当前用户的关于我文档
   *
   * @return
   */
  @GetMapping("/getAboutMeArticle")
  public Article getAboutMeArticle() {
    return articleService.getAboutMeArticle();
  }

  /**
   * /** 发布/暂存文章
   *
   * @param article
   * @return
   */
  @PostMapping("/add")
  @Log(module = "博客管理", value = "新增博客")
  public String addArticle(@RequestBody Article article) {
    articleService.addArticle(article);
    return article.getId();
  }

  /**
   * 修改博客
   *
   * @param article
   * @return
   */
  @PostMapping("/update")
  @Log(module = "博客管理", value = "修改博客")
  public void updateArticle(@RequestBody Article article) {
    if (article == null) {
      throw new BusinessException("参数为空，操作失败");
    }
    articleService.updateArticle(article);
  }

  /**
   * 删除博客
   *
   * @param params
   * @return
   */
  @PostMapping("/delete")
  @Log(module = "博客管理", value = "删除博客")
  public void deleteArticle(@RequestBody List<String> params) {
    if (params == null || params.isEmpty()) {
      throw new BusinessException("传入参数为空");
    }
    articleService.deleteArticle(params);
  }

  /**
   * 将博客信息放入mq
   *
   * @param type
   * @param articleId
   */
  public void sendMsg(String type, int articleId) {
    // 通过消息队列将其存入 Elasticsearch 服务器
    Map<String, Object> map = new HashMap<>(16);
    map.put("articleId", articleId);
    map.put("type", type);
    rabbitTemplate.convertAndSend("article", map);
  }

  /** 刷新博客前端统计数据缓存 */
  public void refreshStatisticCache() {
    // 获取当前登录用户
    User user = SecurityUtils.getCurrentUser();
    // 根据当前用户获取router
    String routing = settingService.getRoutingByUserId(user.getId());
    String url = restTemplateUrl + "/home/refreshStatisticCache?router=" + routing;
    //  发起请求,直接返回对象
    ResponseData<String> responseData = restTemplate.getForObject(url, ResponseData.class);
    if (responseData != null) {
      log.info(responseData.getMessage());
    } else {
      log.info("缓存更新失败");
    }
  }
}
