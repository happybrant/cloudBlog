package com.kongfu.backend.service;

import com.kongfu.backend.dao.ArticleMapper;
import com.kongfu.backend.entity.Article;
import com.kongfu.backend.util.BlogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    public  ArticleMapper articleMapper;

    /**
     * 新增博客
     * @param article
     * @return
     */
    public int addArticle(Article article) {
        if(article == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        return articleMapper.insertArticle(article);
    }

    /**
     * 根据分类分页查找博客
     * @param offset
     * @param limit
     * @return
     */
    public List<Article> findArticles(String category, String createDate, String tag, String title, int offset, int limit){
        return articleMapper.selectArticle(category, createDate, tag, title,  offset, limit);
    }
    /**
     * 查找博客总数
     * @return
     */
    public int findArticleCount(String category, String createDate, String tag, String title){
        return articleMapper.selectArticleCount(category, createDate, tag, title);
    }

    /**
     * 根据id删除博客
     * @param id
     */
    public void deleteArticle(int id){
        articleMapper.updateStatus(id, BlogConstant.DELETE_STATUS);
    }

    public Article findArticleById(int id){
        return articleMapper.selectArticleById(id);
    }

}
