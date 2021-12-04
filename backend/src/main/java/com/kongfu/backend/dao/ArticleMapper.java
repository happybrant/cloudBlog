package com.kongfu.backend.dao;

import com.kongfu.backend.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 付聪
 */
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
     * @param category
     * @param createDate
     * @param tag
     * @param title
     * @param offset
     * @param limit
     * @return
     */
    List<Article> selectArticle(String category, String createDate, String tag, String title, int offset, int limit);

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
    int selectArticleCount(String category, String createDate, String tag, String title);

    /**
     * 修改博客状态：1-正常; 0-删除;
     * @param id
     */
    void updateStatus(int id, int status);
}
