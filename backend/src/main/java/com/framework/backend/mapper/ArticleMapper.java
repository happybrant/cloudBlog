package com.framework.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.backend.model.dto.ArticleQuery;
import com.framework.backend.model.dto.BaseQuery;
import com.framework.backend.model.entity.Article;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 付聪
 */
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
   * @param baseQuery
   * @return
   */
  List<Map<String, Object>> selectArticleByCategory(BaseQuery baseQuery);

  /**
   * 根据创标签对博客进行分组
   *
   * @param baseQuery
   * @return
   */
  List<Map<String, Object>> selectArticleByTag(BaseQuery baseQuery);
}
