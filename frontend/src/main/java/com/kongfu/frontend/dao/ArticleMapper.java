package com.kongfu.frontend.dao;

import com.kongfu.frontend.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {

    /**
     * 新增一条博客
     * @param article
     * @return
     */
    int insertArticle(Article article);

    /**
     * 查找所有博客
     * @param offset
     * @param limit
     * @return
     */
    List<Article> selectArticle(String category, String createDate, String tag, int offset, int limit);

    /**
     * 根据id查找博客
     * @param id
     * @return
     */
    Article selectArticleById(int id);

    /**
     * 查找博客总数
     * @return
     */
    int selectArticleCount(String category, String createDate, String tag);
}
