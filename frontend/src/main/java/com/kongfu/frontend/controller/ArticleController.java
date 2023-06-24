package com.kongfu.frontend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.frontend.common.ResponseResult;
import com.kongfu.frontend.common.ResponseResultCode;
import com.kongfu.frontend.entity.Article;
import com.kongfu.frontend.entity.ArticleDto;
import com.kongfu.frontend.entity.ArticleQuery;
import com.kongfu.frontend.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/** @author 付聪 */
@RequestMapping("/article")
@RestController
public class ArticleController {

  @Resource public ArticleService articleService;

  /**
   * 文章详情页面
   *
   * @param id
   * @return
   */
  @GetMapping("/detail/{id}")
  public ResponseResult<Article> getArticleDetail(@PathVariable("id") int id) {
    ResponseResult<Article> result;
    Article article = articleService.findArticleById(id);
    if (article != null) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", article);
    } else {
      result = new ResponseResult<>(ResponseResultCode.Empty, "操作成功", null);
    }
    return result;
  }

  /**
   * 关于我的文章详情
   *
   * @param router
   * @return
   */
  @GetMapping("/about")
  public ResponseResult<Article> getAboutArticleDetail(@RequestParam("router") String router) {
    ResponseResult<Article> result;
    Article article = articleService.findArticleByRouter(router);
    if (article != null) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", article);
    } else {
      result = new ResponseResult<>(ResponseResultCode.Empty, "操作成功", null);
    }
    return result;
  }
  /**
   * 分页条件查询
   *
   * @param articleQuery
   * @return
   */
  @PostMapping("/getArticlePager")
  public Page<Article> getArticlePager(@RequestBody ArticleQuery articleQuery) {
    return articleService.findArticlePager(articleQuery);
  }

  /**
   * 根据关键字搜索文章
   *
   * @param keyword
   * @return
   * @throws IOException
   */
  @GetMapping("/searchArticles")
  public List<ArticleDto> searchArticles(
      @RequestParam("keyword") String keyword, @RequestParam("router") String router)
      throws IOException {
    return articleService.searchArticleListPager(keyword, router);
  }
}
