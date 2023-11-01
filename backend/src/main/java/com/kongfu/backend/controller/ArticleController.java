package com.kongfu.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.ArticleQuery;
import com.kongfu.backend.model.entity.Article;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.model.vo.LoginToken;
import com.kongfu.backend.service.ArticleService;
import com.kongfu.backend.service.SettingService;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author 付聪 */
@RequestMapping("/article")
@RestController
@Slf4j
public class ArticleController implements BlogConstant {
  @Resource public ArticleService articleService;

  @Value("${restTemplate.url}")
  private String restTemplateUrl;

  @Resource public RabbitTemplate rabbitTemplate;

  @Resource private RestTemplate restTemplate;
  @Resource private SettingService settingService;
  @Resource public HostHolder holder;
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
    if (article == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    ResponseResult<String> result = articleService.addArticle(article);
    if (article.getStatus() != BlogConstant.ABOUT_ME_STATUS
        && article.getStatus() != BlogConstant.ABOUT_ME_UN_PUBLISH_STATUS) {
      // 通过消息队列将其存入 Elasticsearch 服务器
      sendMsg("insert", article.getId());
      // 刷新博客前端统计数据缓存
      refreshStatisticCache();
    }
    return result;
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
    if (article == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    ResponseResult<String> result = articleService.updateArticle(article);
    if (article.getStatus() != BlogConstant.ABOUT_ME_STATUS
        && article.getStatus() != BlogConstant.ABOUT_ME_UN_PUBLISH_STATUS) {
      // 通过消息队列将其存入 Elasticsearch 服务器
      sendMsg("update", article.getId());
      // 刷新博客前端统计数据缓存
      refreshStatisticCache();
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
    if (params == null || params.size() == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "传入参数为空");
    }
    ResponseResult<String> result = articleService.deleteArticle(params);
    for (int i = 0; i < params.size(); i++) {
      sendMsg("delete", params.get(i));
    }
    // 刷新博客前端统计数据缓存
    refreshStatisticCache();
    return result;
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
    LoginToken loginToken = holder.getUser();
    // 根据当前用户获取router
    String routing = settingService.getRoutingByUserId(loginToken.getId());
    String url = restTemplateUrl + "/home/refreshStatisticCache?router=" + routing;
    // 发起请求,直接返回对象
    ResponseResult responseResult = restTemplate.getForObject(url, ResponseResult.class);
    if (responseResult != null) {
      log.info(responseResult.getMessage());
    } else {
      log.info("缓存更新失败");
    }
  }
}
