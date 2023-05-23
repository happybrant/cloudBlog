package com.kongfu.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kongfu.backend.model.dto.ArticleQuery;
import com.kongfu.backend.model.dto.QueryBase;
import com.kongfu.backend.model.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/** @author 付聪 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

  /**
   * 查找所有博客
   *
   * @param query
   * @return
   */
  List<Article> selectArticleList(ArticleQuery query);

  /**
   * 查找博客总数
   *
   * @param query
   * @return
   */
  int selectArticleCount(ArticleQuery query);

  /**
   * 根据id查找博客
   *
   * @param query
   * @return
   */
  Article selectArticleById(ArticleQuery query);

  /**
   * 根据分类对博客进行分组
   *
   * @param queryBase
   * @return
   */
  List<Map<String, Object>> selectArticleByCategory(QueryBase queryBase);

  /**
   * 根据创标签对博客进行分组
   *
   * @param queryBase
   * @return
   */
  List<Map<String, Object>> selectArticleByTag(QueryBase queryBase);
}
