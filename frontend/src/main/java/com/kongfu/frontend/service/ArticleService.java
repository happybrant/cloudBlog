package com.kongfu.frontend.service;

import com.kongfu.frontend.dao.ArticleMapper;
import com.kongfu.frontend.entity.Article;
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
    public List<Article> findArticles(String category, String createDate, String tag, int offset, int limit){
        return articleMapper.selectArticle(category, createDate, tag, offset, limit);
    }
    /**
     * 查找博客总数
     * @return
     */
    public int findArticleCount(String category, String createDate, String tag){
        return articleMapper.selectArticleCount(category, createDate, tag);
    }

    public Article findArticleById(int id){
        return articleMapper.selectArticleById(id);
    }

}
