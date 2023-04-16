package com.kongfu.frontend.dao;

import com.kongfu.frontend.entity.Article;
import com.kongfu.frontend.entity.ArticleQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/** @author 付聪 */
@Mapper
public interface ArticleMapper {

  /**
   * 根据条件分页查询博客
   *
   * @param articleQuery
   * @return
   */
  List<Article> selectArticle(ArticleQuery articleQuery);

  /**
   * 根据id查找博客
   *
   * @param id
   * @return
   */
  Article selectArticleById(int id);
  /**
   * 根据路由查找关于我的博客
   *
   * @param router
   * @return
   */
  Article selectArticleByRouter(String router);

  /**
   * 查找博客总数
   *
   * @param articleQuery
   * @return
   */
  int selectArticleCount(ArticleQuery articleQuery);

  /**
   * 按月对博客进行归档
   *
   * @return
   */
  List<Map<String, Object>> selectArticleGroupByMonth();

  /**
   * 博客列表根据分类和创建时间分类后根据id查找当前博客的前一篇
   *
   * @param id
   * @return
   */
  Article selectPreArticleById(int id);

  /**
   * 博客列表根据分类和创建时间分类后根据id查找当前博客的后一篇
   *
   * @param id
   * @return
   */
  Article selectNextArticleById(int id);
}
