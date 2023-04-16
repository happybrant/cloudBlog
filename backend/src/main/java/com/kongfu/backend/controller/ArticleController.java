package com.kongfu.backend.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.ArticleQuery;
import com.kongfu.backend.model.entity.Article;
import com.kongfu.backend.model.entity.TagArticle;
import com.kongfu.backend.service.ArticleService;
import com.kongfu.backend.service.TagService;
import com.kongfu.backend.util.BlogConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

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

  /**
   * 博客列表页面
   *
   * @return
   */
  @RequestMapping("/list")
  public ResponseResult<Page<Article>> List(
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
      Object pageIndex = map.get("pageIndex");
      Object pageSize = map.get("pageSize");
      if (pageSize != null && pageIndex != null) {
        if (StringUtils.isNumber(pageIndex.toString())
            && StringUtils.isNumber(pageSize.toString())) {
          query.setPageSize((Integer) pageSize);
          query.setPageIndex((Integer) pageIndex);
          query.setStartRow((Integer) pageSize * ((Integer) pageIndex - 1));
        }
      }
      Object status = map.get("status");
      if (status != null && StringUtils.isNumber(status.toString())) {
        query.setStatus((Integer) status);
      }
      Object categoryId = map.get("categoryId");
      if (categoryId != null && StringUtils.isNumber(categoryId.toString())) {
        query.setCategoryId((Integer) categoryId);
      }
      Object title = map.get("title");
      if (title != null) {
        query.setTitle(title.toString());
      }
      Object createTime = map.get("createTime");
      if (createTime != null) {
        query.setCreateTime(createTime.toString());
      }
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
      }
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
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
   * 删除文章
   *
   * @param params
   * @return
   */
  @PostMapping("/delete")
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
