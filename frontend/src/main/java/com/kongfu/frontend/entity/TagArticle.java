package com.kongfu.frontend.entity;


/**
 * 标签博客实体
 */
public class TagArticle {

    private int id;
    private int tagId;
    private int articleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public TagArticle(int tagId, int articleId) {
        this.tagId = tagId;
        this.articleId = articleId;
    }
}
