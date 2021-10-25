package com.kongfu.frontend.entity;

import com.kongfu.frontend.util.BlogUtil;

import java.text.ParseException;

/**
 * 博客类实体
 */
public class Article extends  Entity{

    private String title;
    private String content;
    private String description;
    private int categoryId;
    private int[] tagIds;
    //分类路径
    private String path;
    //处理后的发布日期
    private String publishDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() { return path; }

    public String getPublishDate() throws ParseException {
        return BlogUtil.timeSpan(getCreateTime());
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setPath(String path) { this.path = path;}

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int[] getTagIds() {
        return tagIds;
    }

    public void setTagIds(int[] tagIds) {
        this.tagIds = tagIds;
    }
}
