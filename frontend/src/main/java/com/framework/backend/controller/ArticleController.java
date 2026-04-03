package com.framework.backend.controller;

import com.framework.backend.common.MyPage;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.entity.Article;
import com.framework.backend.entity.ArticleDto;
import com.framework.backend.entity.ArticleQuery;
import com.framework.backend.service.ArticleService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 付聪
 */
@RequestMapping("/article")
@RestController
@ResponseResult
public class ArticleController {

  @Autowired public ArticleService articleService;

  /**
   * 文章详情页面
   *
   * @param id
   * @return
   */
  @GetMapping("/detail/{id}")
  public Article getArticleDetail(@PathVariable("id") int id) {
    return articleService.findArticleById(id);
  }

  /**
   * 关于我的文章详情
   *
   * @param router
   * @return
   */
  @GetMapping("/about")
  public Article getAboutArticleDetail(@RequestParam("router") String router) {
    return articleService.findArticleByRouter(router);
  }

  /**
   * 分页条件查询
   *
   * @param articleQuery
   * @return
   */
  @PostMapping("/getArticlePager")
  public MyPage<Article> getArticlePager(@RequestBody ArticleQuery articleQuery) {
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
