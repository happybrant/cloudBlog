package com.kongfu.backend.entity;

import com.kongfu.backend.util.BlogUtil;

import java.text.ParseException;
import java.util.List;

/**
 * 博客类实体
 * @author 付聪
 */
public class Article extends  Entity{

    private String title;
    private String content;
    private String description;
    private int categoryId;
    private int[] tagIds;
    private List<Tag> tags;
    /**
     * 分类路径
     */
    private String path;

    public int[] getTagIds() {
        return tagIds;
    }
    public void setTagIds(int[] tagIds) {
        this.tagIds = tagIds;
    }
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

    public void setPath(String path) { this.path = path;}

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
